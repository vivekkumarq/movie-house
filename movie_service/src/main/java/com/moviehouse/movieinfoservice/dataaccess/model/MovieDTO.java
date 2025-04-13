package com.moviehouse.movieinfoservice.dataaccess.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class MovieDTO {
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
    private boolean available;
    @JsonIgnoreProperties("movie")
    private List<Rating> ratings;

    public boolean getAvailable(){
        return this.available;
    }
}
