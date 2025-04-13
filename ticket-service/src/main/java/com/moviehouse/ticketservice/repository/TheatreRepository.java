package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {
}
