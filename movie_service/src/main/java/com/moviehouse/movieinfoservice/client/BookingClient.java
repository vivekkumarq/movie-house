package com.moviehouse.movieinfoservice.client;

import com.moviehouse.movieinfoservice.dataaccess.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class BookingClient extends ServiceDiscovery {

    private static final String GET_AVAILABLE_SHOWS_URI = "/show-info-management/show/movie/";
    private static final String GET_AVAILABLE_MOVIES_URI = "/show-info-management/show/available-movies?city={city}";
    private static final String TICKET_SERVICE = "ticket-service";

    @Autowired
    private RestTemplate restTemplate;

    public List<Show> getAvailableShows(UUID movieId, String city) {
        Show[] shows = restTemplate.getForObject(
                serviceUrl(TICKET_SERVICE) + GET_AVAILABLE_SHOWS_URI + movieId + "?city={city}", Show[].class, city);
        return shows == null ? Collections.emptyList() : Arrays.asList(shows);
    }

    @Cacheable("availableMovies")
    public List<UUID> getMovieIdsWithUpcomingShows(String city) {
        List<UUID> movieIds = restTemplate.exchange(
                serviceUrl(TICKET_SERVICE) + GET_AVAILABLE_MOVIES_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UUID>>() {
                },
                city).getBody();
        return movieIds == null ? Collections.emptyList() : movieIds;
    }
}
