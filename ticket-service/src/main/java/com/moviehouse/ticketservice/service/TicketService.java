package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.UserTickets;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface TicketService {
    Ticket bookTicket(Ticket ticket);
    Ticket getTicketById(UUID id);
    Ticket cancelTicket(UUID id);
    UserTickets getAllTicketsByUser(UUID userId);

    List<Ticket> getAllTicketsByShow(UUID showId);

    void updateTicketOfShow();
}
