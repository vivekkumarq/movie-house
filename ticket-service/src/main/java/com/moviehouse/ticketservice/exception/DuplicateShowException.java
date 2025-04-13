package com.moviehouse.ticketservice.exception;

public class DuplicateShowException extends RuntimeException{
    public DuplicateShowException(String message) {
        super(message);
    }
}
