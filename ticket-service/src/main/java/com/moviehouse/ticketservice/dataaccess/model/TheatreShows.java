package com.moviehouse.ticketservice.dataaccess.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreShows {
    private UUID theatreId;
    private String theatreName;
    private Location location;
    @JsonIgnoreProperties("theatre")
    private List<ShowDTO> shows;
}
