package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.exception.SeatLimitException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SeatServiceImpl implements SeatService{
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TheatreService theatreService;

    @Override
    public Seat addSeat(Seat seat) {
        Theatre theatre = theatreService.getTheatreById(seat.getTheatre().getId());
        if(theatre.getSeats().size()==theatre.getSeatCount())
            throw new SeatLimitException("Seat limit reached, cannot add more seat!");
        seat.setTheatre(theatre);
        return seatRepository.save(seat);
    }
    @Override
    public Seat getSeatById(UUID id) {
        return seatRepository.findById(id).get();
    }
    @Override
    public void deleteSeatById(UUID seatId) {
        seatRepository.deleteById(seatId);
    }

    @Override
    public List<Seat> getAllSeatsByTheatre(UUID theatreId) {
        Theatre theatre = theatreService.getTheatreById(theatreId);
        return seatRepository.findByTheatre(theatre);
    }

    @Override
    public List<Seat> addAllSeats(List<Seat> seats) {
        seats.forEach(seat -> {
            Theatre theatre = theatreService.getTheatreById(seat.getTheatre().getId());
            seat.setTheatre(theatre);
        });
        return seatRepository.saveAll(seats);
    }

}
