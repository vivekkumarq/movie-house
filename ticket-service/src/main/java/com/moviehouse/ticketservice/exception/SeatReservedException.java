package com.moviehouse.ticketservice.exception;

public class SeatReservedException extends RuntimeException{
    public SeatReservedException(String message) {
        super(message);
    }
}
