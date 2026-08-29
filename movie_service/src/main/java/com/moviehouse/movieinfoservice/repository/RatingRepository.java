package com.moviehouse.movieinfoservice.repository;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    boolean existsByMovieAndUser(Movie movie, Reference user);

    List<Rating> findByUser(Reference user);

    List<Rating> findByMovie(Movie movie);

    Page<Rating> findAll(Pageable pageable);

    @Query("select coalesce(avg(r.movieRating), 0) from Rating r where r.movie = :movie")
    double averageRatingOf(@Param("movie") Movie movie);
}
