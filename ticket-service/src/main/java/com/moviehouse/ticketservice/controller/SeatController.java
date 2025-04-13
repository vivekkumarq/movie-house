package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seat-info-management/seats")
@CrossOrigin("*")
public class SeatController {
    @Autowired
    private SeatService seatService;

    /*@PostMapping
    public ResponseEntity<Seat> addSeat(@RequestBody Seat seat) {
        return new ResponseEntity<>(seatService.addSeat(seat), HttpStatus.OK);
    }*/
    @PostMapping
    public ResponseEntity<List<Seat>> addAllSeat(@RequestBody List<Seat> seats) {
        return new ResponseEntity<>(seatService.addAllSeats(seats), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable UUID id) {
        return new ResponseEntity<>(seatService.getSeatById(id),HttpStatus.OK);
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Seat>> getAllSeatsByTheatre(@PathVariable UUID theatreId) {
        return new ResponseEntity<>(seatService.getAllSeatsByTheatre(theatreId),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteSeatById(@PathVariable UUID id) {
        seatService.deleteSeatById(id);
    }
}
