package com.moviehouse.ticketservice.dataaccess.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Location {
    private UUID id;
    private String city;
    private String street;
}
