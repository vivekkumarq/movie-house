package com.moviehouse.ticketservice.exception;

public class ShowNotFoundException extends RuntimeException{
    public ShowNotFoundException(String message) {
        super(message);
    }
}
