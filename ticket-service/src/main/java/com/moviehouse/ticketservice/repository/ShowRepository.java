package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {

    @EntityGraph(attributePaths = "theatre")
    Optional<Show> findWithTheatreById(UUID id);

    @EntityGraph(attributePaths = "theatre")
    Page<Show> findAll(Pageable pageable);

    boolean existsByTheatreAndShowDateAndStartTime(Theatre theatre, LocalDate showDate, LocalTime startTime);

    List<Show> findByShowDateAndStartTimeLessThanOrShowDateLessThan(LocalDate showDate, LocalTime startTime, LocalDate showDateBefore);

    @EntityGraph(attributePaths = "theatre")
    @Query("select s from Show s where s.movie = :movie and s.showDate = :date")
    List<Show> findByMovieAndDate(@Param("movie") Reference movie, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = "theatre")
    @Query("select s from Show s where s.movie = :movie "
            + "and (s.showDate > :date or (s.showDate = :date and s.startTime > :time))")
    List<Show> findUpcomingByMovie(@Param("movie") Reference movie,
                                   @Param("date") LocalDate date,
                                   @Param("time") LocalTime time);

    @Query("select distinct s from Show s join fetch s.theatre "
            + "where s.showDate > :date or (s.showDate = :date and s.startTime > :time)")
    List<Show> findUpcoming(@Param("date") LocalDate date, @Param("time") LocalTime time);
}
