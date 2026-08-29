package com.moviehouse.ticketservice.repository;

import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @EntityGraph(attributePaths = {"seats", "show"})
    Optional<Ticket> findWithSeatsById(UUID id);

    List<Ticket> findByShowAndStatusIn(Show show, Collection<TicketStatus> statuses);

    @EntityGraph(attributePaths = {"seats", "show"})
    List<Ticket> findByShow(Show show);

    @EntityGraph(attributePaths = {"seats", "show"})
    List<Ticket> findByUser(Reference user);

    @Query("select t.show.id, count(seat) from Ticket t join t.seats seat "
            + "where t.show in :shows and t.status = :status group by t.show.id")
    List<Object[]> countSeatsByShow(@Param("shows") Collection<Show> shows, @Param("status") TicketStatus status);

    @Modifying(clearAutomatically = true)
    @Query("update Ticket t set t.status = :newStatus where t.status = :currentStatus and t.show in :shows")
    int updateStatusForShows(@Param("shows") Collection<Show> shows,
                             @Param("currentStatus") TicketStatus currentStatus,
                             @Param("newStatus") TicketStatus newStatus);
}
