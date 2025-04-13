package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByTicketsIn(Collection<Ticket> tickets);
    List<Seat> findByTheatre(Theatre theatre);
}
