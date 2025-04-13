package com.moviehouse.ticketservice.controller;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;
import com.moviehouse.ticketservice.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/show-info-management/show")
@CrossOrigin("*")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    public ResponseEntity<Show> createShow(@RequestBody Show show) {
        return new ResponseEntity<>(showService.createShow(show),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(showService.getShowById(id),HttpStatus.OK);
    }

    @GetMapping
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    @DeleteMapping("/{id}")
    public void deleteShowById(@PathVariable("id") UUID id) {
        showService.deleteShowById(id);
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<ShowSeats> getAllAvailableSeats(@PathVariable UUID showId){
        return new ResponseEntity<>(showService.getAllAvailableSeats(showId),HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Show>> getAvailableShows(@PathVariable UUID movieId,@RequestParam String city){
        return new ResponseEntity<>(showService.getAvailableShows(movieId, city),HttpStatus.OK);
    }

    /*@PostMapping("/generateShows/{date}")
    public List<Show> generateShows(@PathVariable String date){
        return showService.generateShowForDate(LocalDate.parse(date));
    }*/
}
