package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.UserClient;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import com.moviehouse.movieinfoservice.exception.DuplicateRateException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import com.moviehouse.movieinfoservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {

    private static final String USER_ALREADY_RATED_THIS_MOVIE = "User already rated this movie";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserClient userClient;

    @Override
    @Transactional
    public Rating addRating(Rating rating) {
        Movie movie = movieService.getMovieById(rating.getMovie().getId());
        Reference user = new Reference(rating.getUser().getId(), userClient.getUser(rating.getUser().getId()).getName());
        if (ratingRepository.existsByMovieAndUser(movie, user)) {
            throw new DuplicateRateException(USER_ALREADY_RATED_THIS_MOVIE);
        }
        rating.setId(null);
        rating.setMovie(movie);
        rating.setUser(user);
        Rating saved = ratingRepository.save(rating);

        movie.setRate(roundToOneDecimal(ratingRepository.averageRatingOf(movie)));
        movieRepository.save(movie);
        return saved;
    }

    @Override
    public Page<Rating> getAllRatings(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    @Override
    public List<Rating> getAllRatingsByMovie(UUID movieId) {
        return ratingRepository.findByMovie(movieService.getMovieById(movieId));
    }

    @Override
    public List<Rating> getAllRatingsByUser(UUID userId) {
        Reference user = new Reference(userId, userClient.getUser(userId).getName());
        return ratingRepository.findByUser(user);
    }

    private float roundToOneDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).floatValue();
    }
}
