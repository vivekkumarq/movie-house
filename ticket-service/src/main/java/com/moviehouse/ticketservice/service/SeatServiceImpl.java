package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.exception.SeatLimitException;
import com.moviehouse.ticketservice.exception.SeatNotFoundException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

    private static final String SEAT_NOT_FOUND = "Seat not found";
    private static final String SEAT_LIMIT_REACHED = "Seat limit reached, cannot add more seats";

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TheatreService theatreService;

    @Override
    @Transactional
    public Seat addSeat(Seat seat) {
        Theatre theatre = theatreService.getTheatreById(seat.getTheatre().getId());
        if (theatre.getSeats().size() >= theatre.getSeatCount()) {
            throw new SeatLimitException(SEAT_LIMIT_REACHED);
        }
        seat.setId(null);
        seat.setTheatre(theatre);
        return seatRepository.save(seat);
    }

    @Override
    public Seat getSeatById(UUID id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new SeatNotFoundException(SEAT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteSeatById(UUID seatId) {
        if (!seatRepository.existsById(seatId)) {
            throw new SeatNotFoundException(SEAT_NOT_FOUND);
        }
        seatRepository.deleteById(seatId);
    }

    @Override
    public List<Seat> getAllSeatsByTheatre(UUID theatreId) {
        return seatRepository.findByTheatre(theatreService.getTheatreById(theatreId));
    }

    @Override
    @Transactional
    public List<Seat> addAllSeats(List<Seat> seats) {
        Map<UUID, Theatre> theatres = new HashMap<>();
        seats.forEach(seat -> {
            seat.setId(null);
            seat.setTheatre(theatres.computeIfAbsent(seat.getTheatre().getId(), theatreService::getTheatreById));
        });
        return seatRepository.saveAll(seats);
    }
}
