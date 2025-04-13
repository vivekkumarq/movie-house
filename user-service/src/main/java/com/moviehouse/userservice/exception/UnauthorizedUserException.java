package com.moviehouse.userservice.exception;

public class UnauthorizedUserException extends RuntimeException{
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
