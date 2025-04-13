package com.moviehouse.movieinfoservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String title;
    @NotNull
    private LocalTime duration;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull
    private String language;
    @NotNull
    private String genre;
    private float rate;
    private UUID poster;
    private UUID cover;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("movie")
    private List<Rating> ratings;
}
