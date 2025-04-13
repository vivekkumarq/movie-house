package com.moviehouse.ticketservice.dataaccess.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Getter
public class Movie {
    private UUID id;
    private String title;
    private LocalTime duration;
    private LocalDate releaseDate;
    private String description;
    private String language;
    private String genre;
    private UUID poster;
    private float rate;
    private boolean available;

    public boolean getAvailable() {
        return this.available;
    }
}
