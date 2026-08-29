package com.moviehouse.movieinfoservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(indexes = @Index(name = "idx_rating_movie", columnList = "movie_id"))
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private float movieRating;

    @Size(max = 2000)
    @Column(columnDefinition = "TEXT")
    private String comment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnoreProperties("ratings")
    private Movie movie;

    @NotNull
    @Valid
    @Type(type = "json")
    @Column(name = "users")
    private Reference user;
}
