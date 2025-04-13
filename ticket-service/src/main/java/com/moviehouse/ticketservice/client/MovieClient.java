package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.Movie;
import com.moviehouse.ticketservice.exception.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class MovieClient extends ServiceDiscovery {

    private static final String GET_MOVIE_URI = "/movie-info-management/movie/";
    private static final String MOVIE_SERVICE = "movie-service";

    @Autowired
    private RestTemplate restTemplate;

    public Movie getMovie(UUID id) {
        System.out.println(serviceUrl(MOVIE_SERVICE));
        try {
            return restTemplate.getForObject(serviceUrl(MOVIE_SERVICE) + GET_MOVIE_URI + id, Movie.class);
//            return restTemplate.getForObject("http://ws-21290.netcracker.com:8090"+ GET_MOVIE_URI + id, Movie.class);
        }catch (RuntimeException e){
            throw new MovieNotFoundException("Movie not found");
        }
    }

    public List<Movie> getAllMovies() {
        System.out.println(serviceUrl(MOVIE_SERVICE));
        Movie[] movie = restTemplate.getForObject(serviceUrl(MOVIE_SERVICE) + GET_MOVIE_URI, Movie[].class);
//        Movie[] movie = restTemplate.getForObject("http://ws-21290.netcracker.com:8090" + GET_MOVIE_URI, Movie[].class);
        return Arrays.asList(movie);
    }
}
