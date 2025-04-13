package com.moviehouse.movieinfoservice.exception;

public class DuplicateRateException extends RuntimeException{
    public DuplicateRateException(String message) {
        super(message);
    }
}
