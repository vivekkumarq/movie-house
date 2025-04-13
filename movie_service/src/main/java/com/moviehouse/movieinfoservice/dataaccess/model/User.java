package com.moviehouse.movieinfoservice.dataaccess.model;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
