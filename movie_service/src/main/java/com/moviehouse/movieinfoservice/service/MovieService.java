package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    Movie addMovie(Movie movie);
    void deleteMovieById(UUID movieid);
    Movie getMovieById(UUID movieId);

    MovieDTO getMovieByIdAndCity(UUID movieId, String city);

    List<Movie> getAllMoviesByCity(String city);

    List<Movie> getAllMovies();

    List<Movie> filter(MovieFilter movieFilter, String city);

}
