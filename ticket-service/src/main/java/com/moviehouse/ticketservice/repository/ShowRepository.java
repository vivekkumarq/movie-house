package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {
    List<Show> findByShowDateAndStartTimeLessThanOrShowDateLessThan(LocalDate showDate, LocalTime startTime, LocalDate showDate1);
    List<Show> findByTheatreAndMovieAndShowDateGreaterThanOrTheatreAndMovieAndShowDateAndStartTimeGreaterThanOrderByShowDateAscStartTimeAsc(Theatre theatre, Reference movie, LocalDate showDate, Theatre theatre1, Reference movie1, LocalDate showDate1, LocalTime startTime);
    List<Show> findByMovieAndShowDateGreaterThanOrMovieAndShowDateAndStartTimeGreaterThan(Reference movie, LocalDate showDate, Reference movie1, LocalDate showDate1, LocalTime startTime);
    List<Show> findByMovieAndShowDateGreaterThanEqual(Reference movie, LocalDate showDate);
    List<Show> findByTheatreAndMovieAndShowDate(Theatre theatre, Reference movie, LocalDate showDate);
    List<Show> findByShowDateAndStartTimeLessThan(LocalDate showDate, LocalTime startTime);
    List<Show> findByTheatreAndShowDateAndStartTime(Theatre theatre, LocalDate showDate, LocalTime startTime);
}
