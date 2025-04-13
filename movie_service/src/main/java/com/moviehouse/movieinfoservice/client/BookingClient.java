package com.moviehouse.movieinfoservice.client;

import com.moviehouse.movieinfoservice.dataaccess.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class BookingClient extends ServiceDiscovery {
    private static final String GET_AVAILABLE_SHOWS_URI = "/show-info-management/show/movie/";
    private static final String TICKET_SERVICE = "ticket-service";
    @Autowired
    private RestTemplate restTemplate;

    public List<Show> getAvailableShows(UUID movieId, String city) {
        System.out.println(serviceUrl(TICKET_SERVICE));
        Show[] shows = restTemplate.getForObject(serviceUrl(TICKET_SERVICE) + GET_AVAILABLE_SHOWS_URI + movieId + "?city={city}", Show[].class, city);
//        Show[] shows = restTemplate.getForObject("http://ws-21290.netcracker.com:8085" + GET_AVAILABLE_SHOWS_URI + movieId + "?city={city}", Show[].class, city);
        return Arrays.asList(shows);
    }
}
