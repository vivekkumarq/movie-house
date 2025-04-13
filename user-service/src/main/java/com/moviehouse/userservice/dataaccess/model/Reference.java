package com.moviehouse.userservice.dataaccess.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Reference {
    private UUID id;
    private String name;
}
