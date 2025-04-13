package com.moviehouse.locationservice.dataaccess.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String city;
    private String street;
    private int pinCode;
}
