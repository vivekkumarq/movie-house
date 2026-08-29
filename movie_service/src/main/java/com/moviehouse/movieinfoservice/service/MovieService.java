package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    Movie addMovie(Movie movie);
    void deleteMovieById(UUID movieId);
    Movie getMovieById(UUID movieId);
    MovieDTO getMovieByIdAndCity(UUID movieId, String city);
    List<MovieSummary> getAllMoviesByCity(String city);
    Page<MovieSummary> getAllMovies(Pageable pageable);
    List<MovieSummary> filter(MovieFilter movieFilter, String city);
}
