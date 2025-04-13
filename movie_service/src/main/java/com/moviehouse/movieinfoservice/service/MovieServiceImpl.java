package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.BookingClient;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.exception.MovieNotFoundException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class MovieServiceImpl implements MovieService {

    private static final String MOVIE_NOT_FOUND = "Movie not found";

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BookingClient bookingClient;

    private Map<String, String> movieAvailability;

    @Override
    public Movie addMovie(Movie movie) {
        movie.setRatings(new ArrayList<>());
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovieById(UUID movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public Movie getMovieById(UUID movieId) {
        return movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException(MOVIE_NOT_FOUND));
    }

    @Override
    public MovieDTO getMovieByIdAndCity(UUID movieId, String city) {
        Movie movie = getMovieById(movieId);
        MovieDTO movieDTO = modelMapper.map(movie, MovieDTO.class);
        movieDTO.setAvailable(bookingClient.getAvailableShows(movie.getId(), city).size()!=0);
        return movieDTO;
    }

    @Override
    public List<Movie> getAllMoviesByCity(String city) {
//        System.out.println(city);
       return movieRepository.findAll().stream()
                .filter(movie -> bookingClient.getAvailableShows(movie.getId(), city).size()!=0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> filter(MovieFilter movieFilter, String city) {
        List<Movie> filteredMovies = getAllMoviesByCity(city);
        if (movieFilter.getLanguage().size() > 0)
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movieFilter.getLanguage().contains(movie.getLanguage()))
                    .collect(Collectors.toList());

        if (movieFilter.getGenre().size() > 0)
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movieFilter.getGenre().contains(movie.getGenre()))
                    .collect(Collectors.toList());
        return filteredMovies;
    }


}
