package com.moviehouse.movieinfoservice.dataaccess.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class Show {
    private UUID id;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private float price;
}
