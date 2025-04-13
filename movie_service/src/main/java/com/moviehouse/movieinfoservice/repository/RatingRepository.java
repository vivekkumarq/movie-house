package com.moviehouse.movieinfoservice.repository;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import com.moviehouse.movieinfoservice.dataaccess.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Rating findByMovieAndUser(Movie movie, Reference user);
    List<Rating> findByUser(Reference user);
    List<Rating> findByMovie(Movie movie);

}
