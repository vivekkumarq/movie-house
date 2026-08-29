package com.moviehouse.movieinfoservice.service;

import com.moviehouse.movieinfoservice.client.UserClient;
import com.moviehouse.movieinfoservice.dataaccess.entity.Movie;
import com.moviehouse.movieinfoservice.dataaccess.entity.Rating;
import com.moviehouse.movieinfoservice.dataaccess.model.Reference;
import com.moviehouse.movieinfoservice.dataaccess.model.User;
import com.moviehouse.movieinfoservice.exception.DuplicateRateException;
import com.moviehouse.movieinfoservice.repository.MovieRepository;
import com.moviehouse.movieinfoservice.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Movie movie;
    private Rating rating;
    private UUID userId;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(UUID.randomUUID());
        movie.setRate(3.0f);

        userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Vivek");

        rating = new Rating();
        rating.setMovie(movie);
        rating.setUser(new Reference(userId, null));
        rating.setMovieRating(5.0f);

        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(userClient.getUser(userId)).thenReturn(user);
    }

    @Test
    void addRatingRefreshesTheMovieAverage() {
        when(ratingRepository.existsByMovieAndUser(movie, new Reference(userId, "Vivek"))).thenReturn(false);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.averageRatingOf(movie)).thenReturn(4.25);

        ratingService.addRating(rating);

        assertThat(movie.getRate()).isEqualTo(4.3f);
        verify(movieRepository).save(movie);
    }

    @Test
    void addRatingResolvesTheReviewerName() {
        when(ratingRepository.existsByMovieAndUser(movie, new Reference(userId, "Vivek"))).thenReturn(false);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.averageRatingOf(movie)).thenReturn(5.0);

        assertThat(ratingService.addRating(rating).getUser().getName()).isEqualTo("Vivek");
    }

    @Test
    void addRatingRejectsASecondRatingFromTheSameUser() {
        when(ratingRepository.existsByMovieAndUser(movie, new Reference(userId, "Vivek"))).thenReturn(true);

        assertThatThrownBy(() -> ratingService.addRating(rating))
                .isInstanceOf(DuplicateRateException.class);
        verify(ratingRepository, never()).save(any(Rating.class));
        verify(movieRepository, never()).save(any(Movie.class));
    }
}
