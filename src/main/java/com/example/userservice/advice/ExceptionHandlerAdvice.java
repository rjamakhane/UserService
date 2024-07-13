package com.example.userservice.advice;

import com.example.userservice.dtos.ExceptionDto;
import com.example.userservice.exception.InvalidCredentialsException;
import com.example.userservice.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidProductIdException(InvalidTokenException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(exceptionDto.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidProductIdException(InvalidCredentialsException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(exceptionDto.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
