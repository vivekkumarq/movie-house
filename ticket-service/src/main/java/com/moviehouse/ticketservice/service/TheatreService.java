package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.TheatreShows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TheatreService {
    Theatre addTheatre(Theatre theatre);
    Theatre getTheatreById(UUID theatreId);
    Page<Theatre> getAllTheatres(Pageable pageable);
    void deleteTheatreById(UUID id);
    List<TheatreShows> getAllTheatreWithShowByMovieAndDateAndCity(UUID movieId, LocalDate date, String city);
}
