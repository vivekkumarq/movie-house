package com.moviehouse.userservice.dataaccess.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Location {
    private UUID id;
    private String city;
    private String street;
    private int pinCode;
}
