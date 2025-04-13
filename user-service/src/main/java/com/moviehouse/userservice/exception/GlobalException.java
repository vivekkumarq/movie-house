package com.moviehouse.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({
            UserNotFoundException.class,
            LocationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DuplicateUsernameException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            UnauthorizedUserException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
