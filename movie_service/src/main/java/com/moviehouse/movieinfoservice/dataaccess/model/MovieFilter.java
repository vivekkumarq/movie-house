package com.moviehouse.movieinfoservice.dataaccess.model;

import lombok.Data;

import java.util.List;

@Data
public class MovieFilter {
    private List<String> genre;
    private List<String> language;
}
