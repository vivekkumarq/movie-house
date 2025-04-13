package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private int seatCount;
    @Type(type = "json")
    private Reference location;
    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("theatre")
    private List<Seat> seats;
    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("theatre")
    private List<Show> shows;
}