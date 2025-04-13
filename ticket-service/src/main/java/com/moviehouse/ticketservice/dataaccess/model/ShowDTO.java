package com.moviehouse.ticketservice.dataaccess.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ShowDTO {
    private UUID id;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private float price;
    private boolean seatAvailable;
    private Reference movie;
}
