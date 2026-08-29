package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.BookingClient;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieFilter;
import com.moviehouse.movieinfoservice.dataaccess.model.MovieSummary;
import com.moviehouse.movieinfoservice.exception.MovieNotFoundException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private BookingClient bookingClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MovieServiceImpl movieService;

    private final UUID kantaraId = UUID.randomUUID();
    private final UUID plane = UUID.randomUUID();

    @Test
    void getAllMoviesByCityAsksTheTicketServiceOnce() {
        when(bookingClient.getMovieIdsWithUpcomingShows("Bengaluru")).thenReturn(Arrays.asList(kantaraId, plane));
        when(movieRepository.findSummariesByIds(Arrays.asList(kantaraId, plane)))
                .thenReturn(Arrays.asList(summary(kantaraId, "Kannada", "Drama"), summary(plane, "English", "Action")));

        List<MovieSummary> movies = movieService.getAllMoviesByCity("Bengaluru");

        assertThat(movies).hasSize(2);
        verify(bookingClient).getMovieIdsWithUpcomingShows("Bengaluru");
    }

    @Test
    void getAllMoviesByCitySkipsTheQueryWhenNothingIsPlaying() {
        when(bookingClient.getMovieIdsWithUpcomingShows("Mysuru")).thenReturn(Collections.emptyList());

        assertThat(movieService.getAllMoviesByCity("Mysuru")).isEmpty();
        verify(movieRepository, never()).findSummariesByIds(anyCollection());
    }

    @Test
    void filterKeepsOnlyTheRequestedGenreAndLanguage() {
        when(bookingClient.getMovieIdsWithUpcomingShows("Bengaluru")).thenReturn(Arrays.asList(kantaraId, plane));
        when(movieRepository.findSummariesByIds(Arrays.asList(kantaraId, plane)))
                .thenReturn(Arrays.asList(summary(kantaraId, "Kannada", "Drama"), summary(plane, "English", "Action")));

        MovieFilter filter = new MovieFilter();
        filter.setGenre(Collections.singletonList("Action"));
        filter.setLanguage(Collections.singletonList("English"));

        assertThat(movieService.filter(filter, "Bengaluru"))
                .extracting(MovieSummary::getId)
                .containsExactly(plane);
    }

    @Test
    void filterWithoutCriteriaReturnsEverythingPlayingInTheCity() {
        when(bookingClient.getMovieIdsWithUpcomingShows("Bengaluru")).thenReturn(Arrays.asList(kantaraId, plane));
        when(movieRepository.findSummariesByIds(Arrays.asList(kantaraId, plane)))
                .thenReturn(Arrays.asList(summary(kantaraId, "Kannada", "Drama"), summary(plane, "English", "Action")));

        assertThat(movieService.filter(new MovieFilter(), "Bengaluru")).hasSize(2);
    }

    @Test
    void getMovieByIdReportsAMissingMovie() {
        when(movieRepository.findWithRatingsById(kantaraId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovieById(kantaraId))
                .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void addMovieIgnoresAnyIdSuppliedByTheCaller() {
        Movie movie = new Movie();
        movie.setId(UUID.randomUUID());
        movie.setTitle("Kantara");
        when(movieRepository.save(movie)).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(movieService.addMovie(movie).getId()).isNull();
    }

    private MovieSummary summary(UUID id, String language, String genre) {
        return new MovieSummary(id, "title", LocalTime.of(2, 0), LocalDate.now(), "description",
                language, genre, 4.0f, null, null);
    }
}
