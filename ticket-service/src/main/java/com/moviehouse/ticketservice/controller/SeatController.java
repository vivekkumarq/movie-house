package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seat-info-management/seats")
@CrossOrigin("*")
@Validated
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping
    public ResponseEntity<List<Seat>> addAllSeats(@RequestBody @NotEmpty List<@Valid Seat> seats) {
        return new ResponseEntity<>(seatService.addAllSeats(seats), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable UUID id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Seat>> getAllSeatsByTheatre(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(seatService.getAllSeatsByTheatre(theatreId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatById(@PathVariable UUID id) {
        seatService.deleteSeatById(id);
        return ResponseEntity.noContent().build();
    }
}
