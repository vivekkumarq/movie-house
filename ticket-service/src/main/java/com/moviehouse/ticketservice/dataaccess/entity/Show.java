package com.moviehouse.ticketservice.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(indexes = {
        @Index(name = "idx_show_theatre", columnList = "theatre_id"),
        @Index(name = "idx_show_date_start_time", columnList = "show_date, start_time")
})
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private LocalDate showDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Positive
    private float price;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theatre_id")
    @JsonIgnoreProperties({"seats", "shows"})
    private Theatre theatre;

    @NotNull
    @Type(type = "json")
    private Reference movie;

    @JsonIgnore
    @OneToMany(mappedBy = "show")
    private List<Ticket> tickets;
}
