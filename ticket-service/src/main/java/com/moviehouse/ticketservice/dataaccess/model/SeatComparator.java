package com.moviehouse.ticketservice.dataaccess.model;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;

import java.util.Comparator;

public class SeatComparator implements Comparator<SeatDTO> {

    @Override
    public int compare(SeatDTO s1, SeatDTO s2) {
        int rowCompare = s1.getSeatNumber().substring(0, 1).compareTo(s2.getSeatNumber().substring(0, 1));
        if (rowCompare == 0) {
            return Integer.parseInt(s1.getSeatNumber().substring(1)) - Integer.parseInt(s2.getSeatNumber().substring(1));
        }
        return rowCompare;
    }
}
