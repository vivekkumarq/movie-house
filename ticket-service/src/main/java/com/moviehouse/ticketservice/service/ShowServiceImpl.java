package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.MovieClient;
import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.Movie;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.SeatComparator;
import com.moviehouse.ticketservice.dataaccess.model.SeatDTO;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.moviehouse.ticketservice.exception.DuplicateShowException;
import com.moviehouse.ticketservice.exception.InvalidShowException;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TheatreNotFoundException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TheatreRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShowServiceImpl implements ShowService {

    private static final String SHOW_COMPLETED = "This show is completed";
    private static final String SHOW_NOT_FOUND = "Show not found";
    private static final String SHOW_ALREADY_CREATED = "Show has already created";
    private static final String THEATRE_NOT_FOUND = "Theatre not found";

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private MovieClient movieClient;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    @Transactional
    public Show createShow(Show show) {
        Theatre theatre = theatreRepository.findById(show.getTheatre().getId())
                .orElseThrow(() -> new TheatreNotFoundException(THEATRE_NOT_FOUND));
        if (showRepository.existsByTheatreAndShowDateAndStartTime(theatre, show.getShowDate(), show.getStartTime())) {
            throw new DuplicateShowException(SHOW_ALREADY_CREATED);
        }
        show.setId(null);
        show.setTheatre(theatre);
        return showRepository.save(show);
    }

    @Override
    public Show getShowById(UUID id) {
        return showRepository.findWithTheatreById(id)
                .orElseThrow(() -> new ShowNotFoundException(SHOW_NOT_FOUND));
    }

    @Override
    public Page<Show> getAllShows(Pageable pageable) {
        return showRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteShowById(UUID id) {
        if (!showRepository.existsById(id)) {
            throw new ShowNotFoundException(SHOW_NOT_FOUND);
        }
        showRepository.deleteById(id);
    }

    @Override
    public List<Seat> getReservedSeats(Show show) {
        List<Ticket> tickets = ticketRepository.findByShowAndStatusIn(show, Collections.singleton(TicketStatus.UPCOMING));
        if (tickets.isEmpty()) {
            return Collections.emptyList();
        }
        return seatRepository.findByTicketsIn(tickets);
    }

    @Override
    public ShowSeats getAllAvailableSeats(UUID showId) {
        Show show = getShowById(showId);
        if (isShowCompleted(show)) {
            throw new InvalidShowException(SHOW_COMPLETED);
        }
        Set<UUID> reservedSeatIds = getReservedSeats(show).stream()
                .map(Seat::getId)
                .collect(Collectors.toSet());
        List<SeatDTO> seats = seatRepository.findByTheatre(show.getTheatre()).stream()
                .map(seat -> new SeatDTO(seat.getId(), seat.getSeatNumber(), seat.getSeatType(),
                        !reservedSeatIds.contains(seat.getId())))
                .sorted(new SeatComparator())
                .collect(Collectors.toList());
        return new ShowSeats(show, seats);
    }

    @Override
    public boolean isShowCompleted(Show show) {
        LocalDate today = LocalDate.now();
        return show.getShowDate().isBefore(today)
                || (show.getShowDate().isEqual(today) && show.getStartTime().isBefore(LocalTime.now()));
    }

    @Override
    public List<Show> getAvailableShows(UUID movieId, String city) {
        Movie movie = movieClient.getMovie(movieId);
        Reference referenceMovie = new Reference(movieId, movie.getTitle());
        return showRepository.findUpcomingByMovie(referenceMovie, LocalDate.now(), LocalTime.now()).stream()
                .filter(show -> show.getTheatre().getLocation().getName().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> getMovieIdsWithUpcomingShows(String city) {
        return showRepository.findUpcoming(LocalDate.now(), LocalTime.now()).stream()
                .filter(show -> show.getTheatre().getLocation().getName().equalsIgnoreCase(city))
                .map(show -> show.getMovie().getId())
                .distinct()
                .collect(Collectors.toList());
    }
}
