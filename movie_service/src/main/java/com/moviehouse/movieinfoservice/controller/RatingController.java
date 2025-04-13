package com.moviehouse.movieinfoservice.controller;

import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import com.moviehouse.movieinfoservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/movie-rating-management/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Rating rating) {
        return new ResponseEntity<>(ratingService.addRating(rating), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        return new ResponseEntity<>(ratingService.getAllRatings(),HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getAllRatingsOfMovie(@PathVariable UUID movieId) {
        return new ResponseEntity<>(ratingService.getAllRatingsByMovie(movieId),HttpStatus.OK);

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getAllRatingsByUser(@PathVariable UUID userId) {
        return new ResponseEntity<>(ratingService.getAllRatingsByUser(userId),HttpStatus.OK);
    }
}
