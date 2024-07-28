package com.example.userservice.services;

import com.example.userservice.configs.KafkaProducer;
import com.example.userservice.dtos.SendEmailDTO;
import com.example.userservice.exception.InvalidCredentialsException;
import com.example.userservice.exception.InvalidPasswordException;
import com.example.userservice.exception.InvalidTokenException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository, KafkaProducer kafkaProducer,ObjectMapper objectMapper){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public User signup(String email, String password, String name) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            //user is already present
            return optionalUser.get();
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);

        // once the signup iscomplete sendm a message to kafka
        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setTo(user.getEmail());
        sendEmailDTO.setFrom("admin@scaler.com");
        sendEmailDTO.setSubject("Welcome to Scaler");
        sendEmailDTO.setMessage("Thanks for joing Scaler");

        try {
            kafkaProducer.sendMessage("sendEmail", objectMapper.writeValueAsString(sendEmailDTO));
        } catch (JsonProcessingException e) {
            System.out.println("Something went wrong while seding message to kafka "+ e.getMessage());
        }
        return userRepository.save(user);
    }

    public Token login(String email, String password) throws InvalidCredentialsException {
        /*
        * check if the user exist with the given email or not
        * if not throw an exception and redirect the user to signup
        * If yes, then compare incoming password with the password stored in the database
        * if the password matches then generate a token and return it
        * */
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            //user is not present
            return null;
        }

        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException("Please enter correct email / password");
        }

        //Login successfull
        Token token = generateToken(user);
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }

    private Token generateToken(User user){
        LocalDate currentTime = LocalDate.now();
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Token token = new Token();
        token.setExpiryDate(expiryDate);

        //token value is randomly generated string of 128 characters
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        token.setIsDeleted(false);

        return token;
    }

    public void logout(String token) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeleted(token, false);
        if(optionalToken.isEmpty()){
            throw new InvalidTokenException("Invalid token");
        }

        Token myToken = optionalToken.get();
        myToken.setIsDeleted(true);
        tokenRepository.save(myToken);
        return;
    }

    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeleted(token, false);
        if(optionalToken.isEmpty()){
            throw new InvalidTokenException("Invalid token");
        }

        Token myToken = optionalToken.get();
        return myToken.getUser();
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return null;
        }
        return optionalUser.get();
    }
}
