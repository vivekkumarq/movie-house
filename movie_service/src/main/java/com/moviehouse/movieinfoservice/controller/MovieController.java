package com.moviehouse.movieinfoservice.controller;

import com.moviehouse.movieinfoservice.dataaccess.model.MovieDTO;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.service.MovieService;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/movie-info-management/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(){
        return new ResponseEntity<>(movieService.getAllMovies(),HttpStatus.OK);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Movie>> getAllMoviesByCity(@PathVariable String city){
        return new ResponseEntity<>(movieService.getAllMoviesByCity(city),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") UUID movieId){
        return new ResponseEntity<>(movieService.getMovieById(movieId), HttpStatus.OK);
    }
    @GetMapping("/{id}/{city}")
    public ResponseEntity<MovieDTO> getMovieByIdAndCity(@PathVariable("id") UUID movieId, @PathVariable String city){
        return new ResponseEntity<>(movieService.getMovieByIdAndCity(movieId, city), HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Movie>> filterMovies(@RequestBody MovieFilter movieFilter, @RequestParam String city){
        return new ResponseEntity<>(movieService.filter(movieFilter, city), HttpStatus.OK);
    }

}