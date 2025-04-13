package com.moviehouse.ticketservice.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.TheatreShows;
import com.moviehouse.ticketservice.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/theatre-info-management/theatre")
@CrossOrigin("*")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping
    public ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatre) {
        return new ResponseEntity<>(theatreService.addTheatre(theatre), HttpStatus.OK);
    }

    @GetMapping
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(theatreService.getTheatreById(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteTheatreById(@PathVariable UUID id) {
        theatreService.deleteTheatreById(id);
    }

    @GetMapping("/movie/{movieId}")
    public List<TheatreShows> getAllTheatreByCityAndMovieAndDate(@PathVariable UUID movieId, @RequestParam String date, @RequestParam String city){
        return theatreService.getAllTheatreWithShowByMovieAndDateAndCity(movieId, LocalDate.parse(date),city);
    }

}
