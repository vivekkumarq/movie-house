package com.moviehouse.ticketservice.dataaccess.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private UUID id;
    private String seatNumber;
    private SeatType seatType;
    private boolean available;
}
