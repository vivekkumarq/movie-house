package com.moviehouse.userservice.exception;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String message) {
        super(message);
    }
}
