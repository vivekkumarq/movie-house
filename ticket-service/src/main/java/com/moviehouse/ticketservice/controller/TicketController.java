package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.moviehouse.ticketservice.dataaccess.model.UserTickets;
import com.moviehouse.ticketservice.repository.SeatRepository;
import com.moviehouse.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/ticket-management/ticket")
@RestController
@CrossOrigin("*")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private SeatRepository seatRepository;

    @PostMapping
    public ResponseEntity<Ticket> bookTicket(@RequestBody Ticket ticket){
        return new ResponseEntity<>(ticketService.bookTicket(ticket), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable UUID id){
        return new ResponseEntity<>(ticketService.getTicketById(id),HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable UUID id){
        return new ResponseEntity<>(ticketService.cancelTicket(id),HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserTickets> getAllTicketsByUser(@PathVariable UUID userId) {
        return new ResponseEntity<>(ticketService.getAllTicketsByUser(userId),HttpStatus.OK);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<Ticket>> getAllTicketsByShow(@PathVariable UUID showId) {
        return new ResponseEntity<>(ticketService.getAllTicketsByShow(showId),HttpStatus.OK);
    }

}
