package com.moviehouse.ticketservice.exception;

public class TheatreNotFoundException extends RuntimeException{
    public TheatreNotFoundException(String message) {
        super(message);
    }
}
