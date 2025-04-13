package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.*;
import com.moviehouse.ticketservice.dataaccess.model.SeatType;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties("tickets")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    @ManyToOne
    @JoinColumn(name="theatre_id")
    @JsonIgnoreProperties({"seats","shows"})
    private Theatre theatre;

    @ManyToMany(mappedBy = "seats")
    private List<Ticket> tickets;

}
