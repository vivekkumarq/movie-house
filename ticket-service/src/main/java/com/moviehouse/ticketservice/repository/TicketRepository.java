package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByShowAndStatusIn(Show show, Collection<TicketStatus> statuses);
    List<Ticket> findByShow(Show show);
    List<Ticket> findByUser(Reference user);

    List<Ticket> findByShowInAndStatus(List<Show> byShowDateAndStartTimeLessThan, TicketStatus status);
}
