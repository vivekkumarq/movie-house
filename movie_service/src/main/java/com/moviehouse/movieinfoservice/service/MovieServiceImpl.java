package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.BookingClient;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary;
import com.moviehouse.movieinfoservice.exception.MovieNotFoundException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private static final String MOVIE_NOT_FOUND = "Movie not found";

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingClient bookingClient;

    @Override
    @Transactional
    public Movie addMovie(Movie movie) {
        movie.setId(null);
        movie.setRatings(new ArrayList<>());
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteMovieById(UUID movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException(MOVIE_NOT_FOUND);
        }
        movieRepository.deleteById(movieId);
    }

    @Override
    public Movie getMovieById(UUID movieId) {
        return movieRepository.findWithRatingsById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND));
    }

    @Override
    public MovieDTO getMovieByIdAndCity(UUID movieId, String city) {
        Movie movie = getMovieById(movieId);
        MovieDTO movieDTO = modelMapper.map(movie, MovieDTO.class);
        movieDTO.setAvailable(!bookingClient.getAvailableShows(movie.getId(), city).isEmpty());
        return movieDTO;
    }

    @Override
    public List<MovieSummary> getAllMoviesByCity(String city) {
        List<UUID> movieIds = bookingClient.getMovieIdsWithUpcomingShows(city);
        if (movieIds.isEmpty()) {
            return Collections.emptyList();
        }
        return movieRepository.findSummariesByIds(movieIds);
    }

    @Override
    public Page<MovieSummary> getAllMovies(Pageable pageable) {
        return movieRepository.findAllSummaries(pageable);
    }

    @Override
    public List<MovieSummary> filter(MovieFilter movieFilter, String city) {
        List<String> genres = movieFilter.getGenre();
        List<String> languages = movieFilter.getLanguage();
        return getAllMoviesByCity(city).stream()
                .filter(movie -> genres == null || genres.isEmpty() || genres.contains(movie.getGenre()))
                .filter(movie -> languages == null || languages.isEmpty() || languages.contains(movie.getLanguage()))
                .collect(Collectors.toList());
    }
}
