package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RatingService {
    Rating addRating(Rating rating);
    Page<Rating> getAllRatings(Pageable pageable);
    List<Rating> getAllRatingsByMovie(UUID movieId);
    List<Rating> getAllRatingsByUser(UUID userId);
}
