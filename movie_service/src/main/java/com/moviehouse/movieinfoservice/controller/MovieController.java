package com.moviehouse.movieinfoservice.controller;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary;
import com.moviehouse.movieinfoservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/movie-info-management/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MovieSummary>> getAllMovies(
            @PageableDefault(size = 50, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<MovieSummary>> getAllMoviesByCity(@PathVariable String city) {
        return ResponseEntity.ok(movieService.getAllMoviesByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @GetMapping("/{id}/{city}")
    public ResponseEntity<MovieDTO> getMovieByIdAndCity(@PathVariable("id") UUID movieId, @PathVariable String city) {
        return ResponseEntity.ok(movieService.getMovieByIdAndCity(movieId, city));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable("id") UUID movieId) {
        movieService.deleteMovieById(movieId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<MovieSummary>> filterMovies(@RequestBody MovieFilter movieFilter,
                                                           @RequestParam String city) {
        return ResponseEntity.ok(movieService.filter(movieFilter, city));
    }
}
