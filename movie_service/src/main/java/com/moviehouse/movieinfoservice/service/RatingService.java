package com.moviehouse.movieinfoservice.service;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;

import java.util.List;
import java.util.UUID;
public interface RatingService {
    Rating addRating(Rating rating);
    List<Rating> getAllRatings();

    List<Rating> getAllRatingsByMovie(UUID movieId);


    List<Rating> getAllRatingsByUser(UUID userId);
}
