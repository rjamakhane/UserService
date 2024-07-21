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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) throws InvalidTokenException {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id){
        System.out.println("User details requested");
        User user = userService.getUser(id);
        return UserDto.from(user);
    }
}
