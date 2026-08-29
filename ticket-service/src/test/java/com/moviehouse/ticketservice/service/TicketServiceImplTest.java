package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.client.UserClient;
import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.SeatType;
import com.moviehouse.ticketservice.dataaccess.model.TicketStatus;
import com.moviehouse.ticketservice.exception.InvalidCancellation;
import com.moviehouse.ticketservice.exception.InvalidShowException;
import com.moviehouse.ticketservice.exception.SeatReservedException;
import com.moviehouse.ticketservice.exception.TicketNotFoundException;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowService showService;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Show show;
    private Seat gold;
    private Seat diamond;

    @BeforeEach
    void setUp() {
        show = new Show();
        show.setId(UUID.randomUUID());
        show.setPrice(200f);

        gold = seat("A1", SeatType.GOLD);
        diamond = seat("B1", SeatType.DIAMOND);
    }

    @Test
    void bookTicketChargesDiamondSeatsAtOneAndAHalfTimesTheShowPrice() {
        Ticket ticket = ticketFor(Arrays.asList(gold, diamond));
        when(showService.getShowById(show.getId())).thenReturn(show);
        when(showService.isShowCompleted(show)).thenReturn(false);
        when(showService.getReservedSeats(show)).thenReturn(Collections.emptyList());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket booked = ticketService.bookTicket(ticket);

        assertThat(booked.getAmount()).isEqualTo(500f);
        assertThat(booked.getStatus()).isEqualTo(TicketStatus.UPCOMING);
    }

    @Test
    void bookTicketRejectsASeatThatIsAlreadyTaken() {
        Ticket ticket = ticketFor(Arrays.asList(gold, diamond));
        when(showService.getShowById(show.getId())).thenReturn(show);
        when(showService.isShowCompleted(show)).thenReturn(false);
        when(showService.getReservedSeats(show)).thenReturn(Collections.singletonList(diamond));

        assertThatThrownBy(() -> ticketService.bookTicket(ticket))
                .isInstanceOf(SeatReservedException.class)
                .hasMessageContaining("B1");
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void bookTicketRejectsAShowThatHasAlreadyStarted() {
        Ticket ticket = ticketFor(Collections.singletonList(gold));
        when(showService.getShowById(show.getId())).thenReturn(show);
        when(showService.isShowCompleted(show)).thenReturn(true);

        assertThatThrownBy(() -> ticketService.bookTicket(ticket))
                .isInstanceOf(InvalidShowException.class);
    }

    @Test
    void cancelTicketRejectsACompletedTicket() {
        Ticket ticket = ticketFor(Collections.singletonList(gold));
        ticket.setId(UUID.randomUUID());
        ticket.setStatus(TicketStatus.COMPLETED);
        when(ticketRepository.findWithSeatsById(ticket.getId())).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketService.cancelTicket(ticket.getId()))
                .isInstanceOf(InvalidCancellation.class);
    }

    @Test
    void cancelTicketMarksAnUpcomingTicketCancelled() {
        Ticket ticket = ticketFor(Collections.singletonList(gold));
        ticket.setId(UUID.randomUUID());
        ticket.setStatus(TicketStatus.UPCOMING);
        when(ticketRepository.findWithSeatsById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        assertThat(ticketService.cancelTicket(ticket.getId()).getStatus()).isEqualTo(TicketStatus.CANCELLED);
    }

    @Test
    void getTicketByIdReportsAMissingTicket() {
        UUID id = UUID.randomUUID();
        when(ticketRepository.findWithSeatsById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(id))
                .isInstanceOf(TicketNotFoundException.class);
    }

    private Ticket ticketFor(List<Seat> seats) {
        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setSeats(seats);
        ticket.setStatus(TicketStatus.COMPLETED);
        return ticket;
    }

    private Seat seat(String number, SeatType type) {
        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setSeatNumber(number);
        seat.setSeatType(type);
        return seat;
    }
}
