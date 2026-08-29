package com.moviehouse.ticketservice.service;

import com.moviehouse.ticketservice.dataaccess.entity.Seat;
import com.moviehouse.ticketservice.dataaccess.entity.Show;
import com.moviehouse.ticketservice.dataaccess.entity.Theatre;
import com.moviehouse.ticketservice.dataaccess.entity.Ticket;
import com.moviehouse.ticketservice.dataaccess.model.SeatDTO;
import com.moviehouse.ticketservice.dataaccess.model.SeatType;
import com.moviehouse.ticketservice.dataaccess.model.ShowSeats;
import com.moviehouse.ticketservice.exception.DuplicateShowException;
import com.moviehouse.ticketservice.exception.InvalidShowException;
import com.moviehouse.ticketservice.exception.ShowNotFoundException;
import com.moviehouse.ticketservice.exception.TheatreNotFoundException;
import com.moviehouse.ticketservice.repository.SeatRepository;
import com.moviehouse.ticketservice.repository.ShowRepository;
import com.moviehouse.ticketservice.repository.TheatreRepository;
import com.moviehouse.ticketservice.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowServiceImplTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ShowServiceImpl showService;

    private Theatre theatre;
    private Show show;

    @BeforeEach
    void setUp() {
        theatre = new Theatre();
        theatre.setId(UUID.randomUUID());
        theatre.setSeatCount(2);

        show = new Show();
        show.setId(UUID.randomUUID());
        show.setTheatre(theatre);
        show.setShowDate(LocalDate.now().plusDays(1));
        show.setStartTime(LocalTime.of(18, 0));
    }

    @Test
    void isShowCompletedIsTrueForYesterday() {
        show.setShowDate(LocalDate.now().minusDays(1));
        assertThat(showService.isShowCompleted(show)).isTrue();
    }

    @Test
    void isShowCompletedIsFalseForTomorrow() {
        assertThat(showService.isShowCompleted(show)).isFalse();
    }

    @Test
    void createShowRejectsASecondShowInTheSameSlot() {
        when(theatreRepository.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        when(showRepository.existsByTheatreAndShowDateAndStartTime(theatre, show.getShowDate(), show.getStartTime()))
                .thenReturn(true);

        assertThatThrownBy(() -> showService.createShow(show))
                .isInstanceOf(DuplicateShowException.class);
        verify(showRepository, never()).save(any(Show.class));
    }

    @Test
    void createShowRejectsAnUnknownTheatre() {
        when(theatreRepository.findById(theatre.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showService.createShow(show))
                .isInstanceOf(TheatreNotFoundException.class);
    }

    @Test
    void getShowByIdReportsAMissingShow() {
        UUID id = UUID.randomUUID();
        when(showRepository.findWithTheatreById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showService.getShowById(id))
                .isInstanceOf(ShowNotFoundException.class);
    }

    @Test
    void getAllAvailableSeatsMarksReservedSeatsUnavailable() {
        Seat taken = seat("A1");
        Seat free = seat("A2");
        Ticket ticket = new Ticket();
        when(showRepository.findWithTheatreById(show.getId())).thenReturn(Optional.of(show));
        when(ticketRepository.findByShowAndStatusIn(any(Show.class), anyCollection()))
                .thenReturn(Collections.singletonList(ticket));
        when(seatRepository.findByTicketsIn(Collections.singletonList(ticket)))
                .thenReturn(Collections.singletonList(taken));
        when(seatRepository.findByTheatre(theatre)).thenReturn(Arrays.asList(free, taken));

        ShowSeats showSeats = showService.getAllAvailableSeats(show.getId());

        List<SeatDTO> seats = showSeats.getSeats();
        assertThat(seats).extracting(SeatDTO::getSeatNumber).containsExactly("A1", "A2");
        assertThat(seats.get(0).isAvailable()).isFalse();
        assertThat(seats.get(1).isAvailable()).isTrue();
    }

    @Test
    void getAllAvailableSeatsRejectsAShowThatHasStarted() {
        show.setShowDate(LocalDate.now().minusDays(1));
        when(showRepository.findWithTheatreById(show.getId())).thenReturn(Optional.of(show));

        assertThatThrownBy(() -> showService.getAllAvailableSeats(show.getId()))
                .isInstanceOf(InvalidShowException.class);
    }

    @Test
    void getReservedSeatsSkipsTheSeatQueryWhenNothingIsBooked() {
        when(ticketRepository.findByShowAndStatusIn(any(Show.class), anyCollection()))
                .thenReturn(Collections.emptyList());

        assertThat(showService.getReservedSeats(show)).isEmpty();
        verify(seatRepository, never()).findByTicketsIn(anyCollection());
    }

    private Seat seat(String number) {
        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setSeatNumber(number);
        seat.setSeatType(SeatType.GOLD);
        seat.setTheatre(theatre);
        return seat;
    }
}
