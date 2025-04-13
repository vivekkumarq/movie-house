package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TheatreService {
    Theatre addTheatre(Theatre theatre);
    Theatre getTheatreById(UUID theatreId);
    List<Theatre> getAllTheatres();
    void deleteTheatreById(UUID id);
    List<TheatreShows> getAllTheatreWithShowByMovieAndDateAndCity(UUID movieId, LocalDate date, String city);
}
