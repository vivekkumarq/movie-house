package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.Movie;
import com.moviehouse.ticketservice.exception.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class MovieClient extends ServiceDiscovery {

    private static final String GET_MOVIE_URI = "/movie-info-management/movie/";
    private static final String MOVIE_SERVICE = "movie-service";
    private static final String MOVIE_NOT_FOUND = "Movie not found";

    @Autowired
    private RestTemplate restTemplate;

    public Movie getMovie(UUID id) {
        try {
            return restTemplate.getForObject(serviceUrl(MOVIE_SERVICE) + GET_MOVIE_URI + id, Movie.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new MovieNotFoundException(MOVIE_NOT_FOUND);
        }
    }

    public List<Movie> getAllMovies() {
        Movie[] movies = restTemplate.getForObject(serviceUrl(MOVIE_SERVICE) + GET_MOVIE_URI, Movie[].class);
        return movies == null ? Collections.emptyList() : Arrays.asList(movies);
    }
}
