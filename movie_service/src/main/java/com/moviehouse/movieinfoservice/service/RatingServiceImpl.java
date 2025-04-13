package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.UserClient;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.dataaccess.model.*;
import com.moviehouse.movieinfoservice.exception.DuplicateRateException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import com.moviehouse.movieinfoservice.repository.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private static final String USER_ALREADY_RATED_THIS_MOVE = "User already rated this movie";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserClient userClient;

    private final static DecimalFormat df = new DecimalFormat("#.#");
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Rating addRating(Rating rating) {
        Movie movie = movieService.getMovieById(rating.getMovie().getId());
        Reference user = new Reference(rating.getUser().getId(), userClient.getUser(rating.getUser().getId()).getName());
        if (ratingRepository.findByMovieAndUser(movie, user) != null)
            throw new DuplicateRateException(USER_ALREADY_RATED_THIS_MOVE);
        float updatedRate = (movie.getRate() * movie.getRatings().size() + rating.getMovieRating())
                / (movie.getRatings().size() + 1);
        movie.setRate(Float.parseFloat(df.format(updatedRate)));
        movieService.addMovie(movie);
        rating.setMovie(movie);
        rating.setUser(user);
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getAllRatingsByMovie(UUID movieId) {
        Movie movie = movieService.getMovieById(movieId);
        return ratingRepository.findByMovie(movie);
    }

    @Override
    public List<Rating> getAllRatingsByUser(UUID userId) {
        Reference user = new Reference(userId, userClient.getUser(userId).getName());
        return ratingRepository.findByUser(user);
    }
}
