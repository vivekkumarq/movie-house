package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.model.SeatType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties("tickets")
@Table(indexes = @Index(name = "idx_seat_theatre", columnList = "theatre_id"))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String seatNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "theatre_id")
    @JsonIgnoreProperties({"seats", "shows"})
    private Theatre theatre;

    @ManyToMany(mappedBy = "seats")
    private List<Ticket> tickets;
}
