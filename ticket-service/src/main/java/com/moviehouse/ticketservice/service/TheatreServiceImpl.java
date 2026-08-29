package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.LocationClient;
import com.moviehouse.ticketservice.client.MovieClient;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.Location;
import com.moviehouse.ticketservice.dataaccess.model.Movie;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.ShowDTO;
import com.moviehouse.ticketservice.dataaccess.model.TheatreShows;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TheatreNotFoundException;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TheatreRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TheatreServiceImpl implements TheatreService {

    private static final String THEATRE_NOT_FOUND = "Theatre not found";

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieClient movieClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocationClient locationClient;

    @Override
    @Transactional
    public Theatre addTheatre(Theatre theatre) {
        theatre.setId(null);
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre getTheatreById(UUID theatreId) {
        return theatreRepository.findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException(THEATRE_NOT_FOUND));
    }

    @Override
    public Page<Theatre> getAllTheatres(Pageable pageable) {
        return theatreRepository.findAll(pageable);
    }

    @Override
    public List<TheatreShows> getAllTheatreWithShowByMovieAndDateAndCity(UUID movieId, LocalDate date, String city) {
        Movie movie = movieClient.getMovie(movieId);
        Reference referenceMovie = new Reference(movie.getId(), movie.getTitle());

        List<Show> shows = showRepository.findByMovieAndDate(referenceMovie, date).stream()
                .filter(show -> show.getTheatre().getLocation().getName().equalsIgnoreCase(city))
                .filter(show -> date.isAfter(LocalDate.now()) || show.getStartTime().isAfter(LocalTime.now()))
                .sorted(Comparator.comparing(Show::getStartTime))
                .collect(Collectors.toList());

        if (shows.isEmpty()) {
            throw new ShowNotFoundException("No shows for the movie " + movie.getTitle() + " on " + date + " in " + city);
        }

        Map<UUID, Long> reservedSeatCount = reservedSeatCountByShow(shows);
        Map<UUID, Theatre> theatres = new LinkedHashMap<>();
        Map<UUID, List<ShowDTO>> showsByTheatre = new LinkedHashMap<>();

        for (Show show : shows) {
            Theatre theatre = show.getTheatre();
            ShowDTO showDTO = modelMapper.map(show, ShowDTO.class);
            showDTO.setSeatAvailable(
                    reservedSeatCount.getOrDefault(show.getId(), 0L) < theatre.getSeatCount());
            theatres.putIfAbsent(theatre.getId(), theatre);
            showsByTheatre.computeIfAbsent(theatre.getId(), id -> new ArrayList<>()).add(showDTO);
        }

        Map<UUID, Location> locationCache = new HashMap<>();
        List<TheatreShows> theatreShows = new ArrayList<>(theatres.size());
        theatres.forEach((theatreId, theatre) -> theatreShows.add(new TheatreShows(
                theatreId,
                theatre.getName(),
                locationCache.computeIfAbsent(theatre.getLocation().getId(), locationClient::getLocation),
                showsByTheatre.get(theatreId))));
        return theatreShows;
    }

    @Override
    @Transactional
    public void deleteTheatreById(UUID id) {
        if (!theatreRepository.existsById(id)) {
            throw new TheatreNotFoundException(THEATRE_NOT_FOUND);
        }
        theatreRepository.deleteById(id);
    }

    private Map<UUID, Long> reservedSeatCountByShow(List<Show> shows) {
        return ticketRepository.countSeatsByShow(shows, TicketStatus.UPCOMING).stream()
                .collect(Collectors.toMap(row -> (UUID) row[0], row -> (Long) row[1]));
    }
}
