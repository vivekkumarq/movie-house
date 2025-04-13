package com.moviehouse.ticketservice.dataaccess.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTickets {
    @JsonIgnoreProperties("password")
    private User user;
    @JsonIgnoreProperties("user")
    private List<Ticket> tickets;
}
