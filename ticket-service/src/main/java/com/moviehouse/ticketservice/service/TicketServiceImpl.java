package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.UserClient;
import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.*;
import com.moviehouse.ticketservice.exception.InvalidCancellation;
import com.moviehouse.ticketservice.exception.SeatReservedException;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TicketNotFoundException;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String SHOW_COMPLETED = "This show is completed";
    private static final String TICKET_NOT_FOUND = "Ticket not found";
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

    private final Map<Seat, ReentrantLock> seatLocks = new HashMap<>();

    @Override
    public Ticket bookTicket(Ticket ticket) {
        List<ReentrantLock> locks = ticket.getSeats().stream()
                .map(seat -> seatLocks.computeIfAbsent(ticket.getSeats().get(0), k -> new ReentrantLock()))
                .collect(Collectors.toList());
        locks.forEach(lock->lock.lock());
        Show show = showService.getShowById(ticket.getShow().getId());
        if (showService.isShowCompleted(show))
            throw new ShowNotFoundException(SHOW_COMPLETED);
        List<UUID> reservedSeat = showService.getReservedSeats(show).stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
        ticket.getSeats().stream().forEach(seat -> {
            if (reservedSeat.contains(seat.getId()))
                throw new SeatReservedException("Seat " + seat.getSeatNumber() + " is already reserved");
        });
        ticket.setShow(showService.getShowById(ticket.getShow().getId()));
        long goldTickets = ticket.getSeats().stream().filter(seat -> seat.getSeatType() == SeatType.GOLD).count();
        long diamondTickets = ticket.getSeats().stream().filter(seat -> seat.getSeatType() == SeatType.DIAMOND).count();
//        System.out.println(goldTickets+" "+diamondTickets);
        float showPrice = ticket.getShow().getPrice();
        ticket.setAmount((float) (goldTickets * showPrice + diamondTickets * showPrice * 1.5));
        Ticket finalTicket = ticketRepository.save(ticket);
        locks.forEach(lock->lock.unlock());
        return finalTicket;
    }

    @Override
    public Ticket getTicketById(UUID id) {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));
    }

    @Override
    public Ticket cancelTicket(UUID id) {
        Ticket ticket = getTicketById(id);
        if (ticket.getStatus() == TicketStatus.COMPLETED)
            throw new InvalidCancellation("Cancellation not allowed");
        ticket.setStatus(TicketStatus.CANCELLED);
        return ticketRepository.save(ticket);
    }

    @Override
    public UserTickets getAllTicketsByUser(UUID userId) {
        User user = userClient.getUser(userId);
        List<Ticket> tickets = ticketRepository.findByUser(modelMapper.map(user, Reference.class));
        Collections.reverse(tickets);
        UserTickets userTickets = new UserTickets(user, tickets);
        return userTickets;
    }

    @Override
    public List<Ticket> getAllTicketsByShow(UUID showId) {
        Show show = showService.getShowById(showId);
        return ticketRepository.findByShow(show);
    }

    @Override
    @Scheduled(cron = "0 0/30 8/4 * * *")
    @Transactional
    public void updateTicketOfShow() {
        System.out.println("CRON IS RUNNING");
        /*showRepository.findByShowDateAndStartTimeLessThan(LocalDate.now(),LocalTime.now()).stream()
                .map(show -> {
                            show.setTickets(show.getTickets().stream()
                                    .filter(ticket -> ticket.getStatus() != TicketStatus.CANCELLED)
                                    .map(ticket -> {
                                        ticket.setStatus(TicketStatus.COMPLETED);
                                        return ticket;
                                    }).collect(Collectors.toList()));
                            return show;
                        }
                ).forEach(showRepository::save);*/
//        System.out.println("SHOWS");
//        showRepository.findByShowDateAndStartTimeLessThan(LocalDate.now(), LocalTime.now())
//                .stream().map(show -> show.getId()).forEach(System.out::println);
//        System.out.println("TICKETS");
//        ticketRepository.findByShowInAndStatus(showRepository.findByShowDateAndStartTimeLessThan(LocalDate.now(), LocalTime.now()), TicketStatus.UPCOMING)
//                .stream().map(ticket -> ticket.getId()).forEach(System.out::println);
        ticketRepository.findByShowInAndStatus(showRepository.findByShowDateAndStartTimeLessThanOrShowDateLessThan(LocalDate.now(), LocalTime.now(), LocalDate.now()), TicketStatus.UPCOMING)
                .stream().forEach(ticket -> {
//                    System.out.println("UPDATE TICKET");
                    ticket.setStatus(TicketStatus.COMPLETED);
                    ticketRepository.save(ticket);
                });
    }

}
