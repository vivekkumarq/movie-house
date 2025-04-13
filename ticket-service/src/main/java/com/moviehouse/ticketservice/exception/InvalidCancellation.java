package com.moviehouse.ticketservice.exception;

public class InvalidCancellation extends RuntimeException{
    public InvalidCancellation(String message) {
        super(message);
    }
}
