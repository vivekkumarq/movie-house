package com.moviehouse.locationservice.exception;

public class LocationNotFound extends RuntimeException{
    public LocationNotFound(String message) {
        super(message);
    }
}
