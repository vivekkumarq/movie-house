package com.moviehouse.movieinfoservice.repository;

import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    String SUMMARY = "select new com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary("
            + "m.id, m.title, m.duration, m.releaseDate, m.description, m.language, m.genre, m.rate, m.poster, m.cover) "
            + "from Movie m";

    @Query("select m from Movie m left join fetch m.ratings where m.id = :id")
    Optional<Movie> findWithRatingsById(@Param("id") UUID id);

    @Query(value = SUMMARY, countQuery = "select count(m) from Movie m")
    Page<MovieSummary> findAllSummaries(Pageable pageable);

    @Query(SUMMARY + " where m.id in :ids")
    List<MovieSummary> findSummariesByIds(@Param("ids") Collection<UUID> ids);
}
