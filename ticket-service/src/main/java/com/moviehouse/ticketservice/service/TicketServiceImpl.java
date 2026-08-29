package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.UserClient;
import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.Reference;
import com.moviehouse.ticketservice.dataaccess.model.SeatType;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.moviehouse.ticketservice.dataaccess.model.User;
import com.moviehouse.ticketservice.dataaccess.model.UserTickets;
import com.moviehouse.ticketservice.exception.InvalidCancellation;
import com.moviehouse.ticketservice.exception.InvalidShowException;
import com.moviehouse.ticketservice.exception.SeatReservedException;
import com.moviehouse.ticketservice.exception.TicketNotFoundException;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

    private static final String SHOW_COMPLETED = "This show is completed";
    private static final String TICKET_NOT_FOUND = "Ticket not found";
    private static final float DIAMOND_PRICE_MULTIPLIER = 1.5f;

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowService showService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ShowRepository showRepository;

    // Guards double booking of a seat within this instance only; a multi-instance
    // deployment needs a database constraint or a distributed lock.
    private final ConcurrentMap<UUID, ReentrantLock> seatLocks = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public Ticket bookTicket(Ticket ticket) {
        List<ReentrantLock> locks = lockSeats(ticket.getSeats());
        try {
            Show show = showService.getShowById(ticket.getShow().getId());
            if (showService.isShowCompleted(show)) {
                throw new InvalidShowException(SHOW_COMPLETED);
            }
            Set<UUID> reservedSeats = showService.getReservedSeats(show).stream()
                    .map(Seat::getId)
                    .collect(Collectors.toSet());
            ticket.getSeats().stream()
                    .filter(seat -> reservedSeats.contains(seat.getId()))
                    .findFirst()
                    .ifPresent(seat -> {
                        throw new SeatReservedException("Seat " + seat.getSeatNumber() + " is already reserved");
                    });

            ticket.setId(null);
            ticket.setShow(show);
            ticket.setStatus(TicketStatus.UPCOMING);
            ticket.setAmount(priceOf(ticket.getSeats(), show.getPrice()));
            return ticketRepository.save(ticket);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
    }

    @Override
    public Ticket getTicketById(UUID id) {
        return ticketRepository.findWithSeatsById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));
    }

    @Override
    @Transactional
    public Ticket cancelTicket(UUID id) {
        Ticket ticket = getTicketById(id);
        if (ticket.getStatus() != TicketStatus.UPCOMING) {
            throw new InvalidCancellation("Cancellation not allowed");
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        return ticketRepository.save(ticket);
    }

    @Override
    public UserTickets getAllTicketsByUser(UUID userId) {
        User user = userClient.getUser(userId);
        List<Ticket> tickets = ticketRepository.findByUser(modelMapper.map(user, Reference.class));
        Collections.reverse(tickets);
        return new UserTickets(user, tickets);
    }

    @Override
    public List<Ticket> getAllTicketsByShow(UUID showId) {
        return ticketRepository.findByShow(showService.getShowById(showId));
    }

    @Override
    @Scheduled(cron = "${ticket.completion-cron:0 0/30 8/4 * * *}")
    @Transactional
    public void updateTicketOfShow() {
        List<Show> startedShows = showRepository.findByShowDateAndStartTimeLessThanOrShowDateLessThan(
                LocalDate.now(), LocalTime.now(), LocalDate.now());
        if (startedShows.isEmpty()) {
            return;
        }
        int updated = ticketRepository.updateStatusForShows(
                startedShows, TicketStatus.UPCOMING, TicketStatus.COMPLETED);
        log.info("Marked {} tickets as completed across {} shows", updated, startedShows.size());
    }

    private List<ReentrantLock> lockSeats(List<Seat> seats) {
        List<ReentrantLock> locks = seats.stream()
                .map(Seat::getId)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .map(seatId -> seatLocks.computeIfAbsent(seatId, id -> new ReentrantLock()))
                .collect(Collectors.toList());
        locks.forEach(ReentrantLock::lock);
        return locks;
    }

    private float priceOf(List<Seat> seats, float showPrice) {
        long goldSeats = seats.stream().filter(seat -> seat.getSeatType() == SeatType.GOLD).count();
        long diamondSeats = seats.stream().filter(seat -> seat.getSeatType() == SeatType.DIAMOND).count();
        return goldSeats * showPrice + diamondSeats * showPrice * DIAMOND_PRICE_MULTIPLIER;
    }
}
