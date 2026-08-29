package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.UserTickets;
import com.moviehouse.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/ticket-management/ticket")
@RestController
@CrossOrigin("*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> bookTicket(@Valid @RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketService.bookTicket(ticket), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.cancelTicket(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserTickets> getAllTicketsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ticketService.getAllTicketsByUser(userId));
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<Ticket>> getAllTicketsByShow(@PathVariable UUID showId) {
        return ResponseEntity.ok(ticketService.getAllTicketsByShow(showId));
    }
}
