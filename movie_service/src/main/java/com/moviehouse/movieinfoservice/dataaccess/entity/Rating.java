package com.moviehouse.movieinfoservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private float movieRating;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnoreProperties("ratings")
    private Movie movie;

    @Type(type = "json")
    @Column(name = "users")
    private Reference user;

}
