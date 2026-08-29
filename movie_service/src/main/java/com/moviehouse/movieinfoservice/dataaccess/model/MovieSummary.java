package com.moviehouse.movieinfoservice.dataaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSummary {
    private UUID id;
    private String title;
    private LocalTime duration;
    private LocalDate releaseDate;
    private String description;
    private String language;
    private String genre;
    private float rate;
    private UUID poster;
    private UUID cover;
}
