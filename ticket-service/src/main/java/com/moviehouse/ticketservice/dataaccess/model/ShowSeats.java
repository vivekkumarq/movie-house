package com.moviehouse.ticketservice.dataaccess.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ShowSeats {
    @JsonIgnoreProperties({
            "tickets"
    })
    private Show show;
    private List<SeatDTO> seats;
}
