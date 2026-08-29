package com.moviehouse.movieinfoservice.controller;

import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/movie-rating-management/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> addRating(@Valid @RequestBody Rating rating) {
        return new ResponseEntity<>(ratingService.addRating(rating), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Rating>> getAllRatings(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ratingService.getAllRatings(pageable));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getAllRatingsOfMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByMovie(movieId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getAllRatingsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByUser(userId));
    }
}
