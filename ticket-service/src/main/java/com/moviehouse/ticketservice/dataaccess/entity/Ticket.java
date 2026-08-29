package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(indexes = {
        @Index(name = "idx_ticket_show_status", columnList = "show_id, status")
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Valid
    @Type(type = "json")
    @Column(name = "users")
    private Reference user;

    private float amount;

    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "show_id")
    @JsonIgnoreProperties("tickets")
    private Show show;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "reserved_seat",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    @JsonIgnoreProperties("theatre")
    private List<Seat> seats;
}
