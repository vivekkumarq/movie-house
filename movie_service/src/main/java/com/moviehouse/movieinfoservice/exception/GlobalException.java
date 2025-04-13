package com.moviehouse.movieinfoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({
            MovieNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({
            DuplicateRateException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
