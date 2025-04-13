package com.moviehouse.imageservice.dataaccess.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String path;
}
