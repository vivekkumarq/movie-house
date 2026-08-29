package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;
import com.moviehouse.ticketservice.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/show-info-management/show")
@CrossOrigin("*")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    public ResponseEntity<Show> createShow(@Valid @RequestBody Show show) {
        return new ResponseEntity<>(showService.createShow(show), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Show>> getAllShows(
            @PageableDefault(size = 50, sort = {"showDate", "startTime"}) Pageable pageable) {
        return ResponseEntity.ok(showService.getAllShows(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowById(@PathVariable("id") UUID id) {
        showService.deleteShowById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<ShowSeats> getAllAvailableSeats(@PathVariable UUID showId) {
        return ResponseEntity.ok(showService.getAllAvailableSeats(showId));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Show>> getAvailableShows(@PathVariable UUID movieId, @RequestParam String city) {
        return ResponseEntity.ok(showService.getAvailableShows(movieId, city));
    }

    @GetMapping("/available-movies")
    public ResponseEntity<List<UUID>> getMovieIdsWithUpcomingShows(@RequestParam String city) {
        return ResponseEntity.ok(showService.getMovieIdsWithUpcomingShows(city));
    }
}
