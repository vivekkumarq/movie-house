package com.moviehouse.ticketservice.dataaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reference {
    private UUID id;
    private String name;
}
