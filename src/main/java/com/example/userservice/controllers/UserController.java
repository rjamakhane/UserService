package com.example.userservice.controllers;

import com.example.userservice.dtos.*;
import com.example.userservice.exception.InvalidCredentialsException;
import com.example.userservice.exception.InvalidPasswordException;
import com.example.userservice.exception.InvalidTokenException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user = userService.signup(signUpRequestDto.getEmail(), signUpRequestDto.getPassword(), signUpRequestDto.getName());
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) throws InvalidCredentialsException {
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        return loginResponseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> Logout(@RequestBody LogOutRequestDto logOutRequestDto) throws InvalidTokenException {
        userService.logout(logOutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
