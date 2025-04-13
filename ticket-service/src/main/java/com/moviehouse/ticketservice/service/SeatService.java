package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;

import java.util.List;
import java.util.UUID;

public interface SeatService {
    Seat addSeat(Seat seat);
    Seat getSeatById(UUID id);
    void deleteSeatById(UUID seatId);
    List<Seat> getAllSeatsByTheatre(UUID theatreId);

    List<Seat> addAllSeats(List<Seat> seats);
}
