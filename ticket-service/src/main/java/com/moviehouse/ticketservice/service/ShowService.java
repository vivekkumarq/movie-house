package com.moviehouse.ticketservice.service;


import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ShowService {
    Show createShow(Show show);
    Show getShowById(UUID id);
    List<Show> getAllShows();
    void deleteShowById(UUID id);
    ShowSeats getAllAvailableSeats(UUID showId);

    List<Seat> getReservedSeats(Show show);

    boolean isShowCompleted(Show show);

    List<Show> getAvailableShows(UUID movieId, String city);

    List<Show> generateShowForDate(LocalDate localDate);

}
