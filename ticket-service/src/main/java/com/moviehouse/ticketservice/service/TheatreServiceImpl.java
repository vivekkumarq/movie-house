package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.LocationClient;
import com.moviehouse.ticketservice.client.MovieClient;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.*;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TheatreNotFoundException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TheatreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TheatreServiceImpl implements TheatreService {

    private static final String THEATRE_NOT_FOUND = "Theatre not found";

    @Autowired
    private TheatreRepository theatreRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private MovieClient movieClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShowService showService;
    @Autowired
    private LocationClient locationClient;

    @Override
    public Theatre addTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre getTheatreById(UUID theatreId) {
        return theatreRepository.findById(theatreId).orElseThrow(() -> new TheatreNotFoundException(THEATRE_NOT_FOUND));
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public List<TheatreShows> getAllTheatreWithShowByMovieAndDateAndCity(UUID movieId, LocalDate date, String city) {
        Movie movie = movieClient.getMovie(movieId);
        Reference referenceMovie = new Reference(movie.getId(), movie.getTitle());

        List<TheatreShows> theatreShows = theatreRepository.findAll().stream()
                .filter(theatre -> theatre.getLocation().getName().equalsIgnoreCase(city))
                .map(theatre -> new TheatreShows(theatre.getId(), theatre.getName(), locationClient.getLocation(theatre.getLocation().getId()),
                        showRepository.findByTheatreAndMovieAndShowDate(theatre, referenceMovie, date).stream()
                                .filter(show -> date.compareTo(LocalDate.now()) > 0 || show.getStartTime().compareTo(LocalTime.now()) > 0)
                                .sorted(Comparator.comparing(Show::getStartTime))
                                .map(show -> {
                                    ShowDTO showDTO = modelMapper.map(show, ShowDTO.class);
                                    showDTO.setSeatAvailable(showService.getReservedSeats(show).size() != show.getTheatre().getSeatCount());
                                    return showDTO;
                                })
                                .collect(Collectors.toList())
                ))
                .filter(theatreShow -> theatreShow.getShows().size() > 0)
                .collect(Collectors.toList());
/*
        List<TheatreShows> theatreShows = theatreRepository.findAll().stream()
                .filter(theatre -> theatre.getLocation().getName().equalsIgnoreCase(city))
                .map(theatre -> new TheatreShows(theatre.getId(), theatre.getName(), theatre.getLocation(),
                        showRepository.findByTheatreAndMovieAndShowDateGreaterThanOrTheatreAndMovieAndShowDateAndStartTimeGreaterThanOrderByShowDateAscStartTimeAsc(theatre,referenceMovie,LocalDate.now(),theatre,referenceMovie,LocalDate.now(),LocalTime.now())
                                .stream().map(show -> {
                                    ShowDTO showDTO = modelMapper.map(show,ShowDTO.class);
                                    showDTO.setSeatAvailable(showService.getReservedSeats(show).size()!=show.getTheatre().getSeatCount());
                                    return showDTO;
                                })
                                .collect(Collectors.toList())
                ))
                .filter(theatreShow -> theatreShow.getShows().size() > 0)
                .collect(Collectors.toList());
*/

        if (theatreShows.size() == 0)
            throw new ShowNotFoundException("No shows for the movie " + movie.getTitle() + " on " + date + " in " + city);
        return theatreShows;
    }

    @Override
    public void deleteTheatreById(UUID id) {
        theatreRepository.deleteById(id);
    }
}
