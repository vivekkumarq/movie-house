package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ShowService {
    Show createShow(Show show);
    Show getShowById(UUID id);
    Page<Show> getAllShows(Pageable pageable);
    void deleteShowById(UUID id);
    ShowSeats getAllAvailableSeats(UUID showId);
    List<Seat> getReservedSeats(Show show);
    boolean isShowCompleted(Show show);
    List<Show> getAvailableShows(UUID movieId, String city);
    List<UUID> getMovieIdsWithUpcomingShows(String city);
}
