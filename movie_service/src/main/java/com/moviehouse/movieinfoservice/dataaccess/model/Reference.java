package com.moviehouse.movieinfoservice.dataaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Reference {
    private UUID id;
    private String name;
}
