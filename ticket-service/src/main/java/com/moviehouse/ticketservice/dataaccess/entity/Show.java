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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Show{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private float price;
    @ManyToOne
    @JoinColumn(name="theatre_id")
    @JsonIgnoreProperties({"seats","shows"})
    private Theatre theatre;
    @Type(type = "json")
    private Reference movie;
    @OneToMany(mappedBy = "show")
    @JsonIgnoreProperties("show")
    private List<Ticket> tickets;
}
