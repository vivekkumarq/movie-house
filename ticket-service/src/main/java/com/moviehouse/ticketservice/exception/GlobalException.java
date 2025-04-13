package com.moviehouse.ticketservice.exception;

import com.moviehouse.ticketservice.dataaccess.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({
            DuplicateShowException.class,
            SeatLimitException.class,
            InvalidCancellation.class,
            InvalidShowException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            ShowNotFoundException.class,
            TheatreNotFoundException.class,
            TicketNotFoundException.class,
            UserNotFound.class,
            MovieNotFoundException.class,
            LocationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFundException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            SeatReservedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
