package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.TheatreShows;
import com.moviehouse.ticketservice.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/theatre-info-management/theatre")
@CrossOrigin("*")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping
    public ResponseEntity<Theatre> addTheatre(@Valid @RequestBody Theatre theatre) {
        return new ResponseEntity<>(theatreService.addTheatre(theatre), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Theatre>> getAllTheatres(@PageableDefault(size = 50, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(theatreService.getAllTheatres(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatreById(@PathVariable UUID id) {
        theatreService.deleteTheatreById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<TheatreShows>> getAllTheatreByCityAndMovieAndDate(
            @PathVariable UUID movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String city) {
        return ResponseEntity.ok(theatreService.getAllTheatreWithShowByMovieAndDateAndCity(movieId, date, city));
    }
}
