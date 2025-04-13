package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.MovieClient;
import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.*;
import com.moviehouse.ticketservice.exception.DuplicateShowException;
import com.moviehouse.ticketservice.exception.InvalidShowException;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TheatreNotFoundException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TheatreRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {

    private static final String SHOW_COMPLETED = "This show is completed";
    private static final String SHOW_NOT_FOUND = "Show not found";
    private static final String SHOW_ALREADY_CREATED = "Show has already created";

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieClient movieClient;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Override
    public Show createShow(Show show) {
        Theatre theatre = theatreRepository.findById(show.getTheatre().getId()).orElseThrow(() -> new TheatreNotFoundException("Theatre not found"));
        if (showRepository.findByTheatreAndShowDateAndStartTime(theatre, show.getShowDate(), show.getStartTime()).size() != 0) {
            throw new DuplicateShowException(SHOW_ALREADY_CREATED);
        }
        show.setTheatre(theatre);
        return showRepository.save(show);
    }

    @Override
    public Show getShowById(UUID id) {
        return showRepository.findById(id).orElseThrow(() -> new ShowNotFoundException(SHOW_NOT_FOUND));
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public void deleteShowById(UUID id) {
        showRepository.deleteById(id);
    }

    @Override
    public List<Seat> getReservedSeats(Show show) {
//        return ticketRepository.findByShowAndStatusIn(show, Collections.singleton(TicketStatus.UPCOMING)).stream()
//                .collect(ArrayList::new, (seats, ticket) -> seats.addAll(ticket.getSeats()), ArrayList::addAll);
        return seatRepository.findByTicketsIn(ticketRepository.findByShowAndStatusIn(show, Collections.singleton(TicketStatus.UPCOMING)));
    }

    @Override
    public ShowSeats getAllAvailableSeats(UUID showId) {
        Show show = showRepository.findById(showId).orElseThrow(() -> new ShowNotFoundException(SHOW_NOT_FOUND));
        if (isShowCompleted(show))
            throw new InvalidShowException(SHOW_COMPLETED);
        List<Seat> reservedSeats = getReservedSeats(show);
        List<SeatDTO> seats = show.getTheatre().getSeats().stream()
                .map(seat -> new SeatDTO(seat.getId(), seat.getSeatNumber(), seat.getSeatType(), (reservedSeats.contains(seat) ? false : true)))
                .sorted(new SeatComparator())
                .collect(Collectors.toList());
        return new ShowSeats(show, seats);
    }

    @Override
    public boolean isShowCompleted(Show show) {
        return show.getShowDate().compareTo(LocalDate.now()) < 0 || (show.getShowDate().compareTo(LocalDate.now()) == 0 && show.getStartTime().compareTo(LocalTime.now()) < 0);
    }

    @Override
    public List<Show> getAvailableShows(UUID movieId, String city) {
//        System.out.println(movieId);
        Movie movie = movieClient.getMovie(movieId);
        Reference referenceMovie = new Reference(movieId, movie.getTitle());
        return showRepository.findByMovieAndShowDateGreaterThanOrMovieAndShowDateAndStartTimeGreaterThan(referenceMovie, LocalDate.now(), referenceMovie, LocalDate.now(), LocalTime.now())
                .stream().filter(show -> show.getTheatre().getLocation().getName().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Show> generateShowForDate(LocalDate localDate) {
        List<Show> shows = new ArrayList<>();
        Map<String, List<LocalTime>> showTime = new HashMap<>();

        showTime.put("startTime", Arrays.asList(
                LocalTime.of(8, 30),
                LocalTime.of(12, 30),
                LocalTime.of(16, 00),
                LocalTime.of(20, 00)));

        showTime.put("endTime", Arrays.asList(
                LocalTime.of(11, 30),
                LocalTime.of(15, 30),
                LocalTime.of(17, 00),
                LocalTime.of(23, 00)));

        List<Float> prices = Arrays.asList(200f, 230f, 250f, 300f, 350f, 450f, 500f, 550f, 600f, 650f);

        List<Reference> movies = movieClient.getAllMovies().stream().filter(movie -> movie.getReleaseDate().compareTo(LocalDate.of(2022, 9, 1)) > 0)
                .map(movie -> new Reference(movie.getId(), movie.getTitle()))
                .collect(Collectors.toList());
        int k = 0;
        for (Theatre theatre : theatreRepository.findAll().subList(28,32)) {
            System.out.println(theatre.getName());
            Reference movie = movies.get(k);
//            System.out.println(movie.getName());
            int i = (int) (3 * Math.random());
//            System.out.println(i);
            for (; i < 4; i++) {
                Show show = new Show();
                show.setShowDate(localDate);
                show.setStartTime(showTime.get("startTime").get(i));
//                System.out.println(showTime.get("startTime").get(i));
                show.setEndTime(showTime.get("endTime").get(i));
                show.setPrice(prices.stream().skip((int) (prices.size() * Math.random())).findAny().get());
                show.setTheatre(theatre);
                show.setMovie(movie);
//                System.out.println(show);
                show.setTickets(new ArrayList<>());
                shows.add(show);
//                shows.add(showRepository.save(show));
            }
            k = ++k % 7;
        }
//        return showRepository.saveAll(shows);
        return shows;
    }
}
