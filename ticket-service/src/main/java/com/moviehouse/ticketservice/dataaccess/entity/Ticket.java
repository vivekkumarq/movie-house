package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.*;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
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
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Type(type = "json")
    @Column(name = "users")
    private Reference user;
    private float amount;
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;
    @ManyToOne
    @JsonIgnoreProperties("tickets")
    private Show show;
    @ManyToMany
    @JoinTable(
            name = "reserved_seat",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    @JsonIgnoreProperties("theatre")
    private List<Seat> seats;

}
