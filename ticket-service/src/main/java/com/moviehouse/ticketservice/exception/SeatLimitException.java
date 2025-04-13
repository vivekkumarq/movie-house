package com.moviehouse.ticketservice.exception;

public class SeatLimitException extends RuntimeException{
    public SeatLimitException(String message) {
        super(message);
    }
}
