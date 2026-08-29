package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @NotBlank
    private String name;

    @Positive
    private int seatCount;

    @NotNull
    @Type(type = "json")
    private Reference location;

    @JsonIgnore
    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @JsonIgnore
    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<Show> shows;
}
