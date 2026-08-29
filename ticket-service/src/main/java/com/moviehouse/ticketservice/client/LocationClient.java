package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.Location;
import com.moviehouse.ticketservice.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class LocationClient extends ServiceDiscovery {

    private static final String GET_LOCATION_URI = "/location-info-management/location/";
    private static final String LOCATION_SERVICE = "location-service";
    private static final String LOCATION_NOT_FOUND = "Location not found";

    @Autowired
    private RestTemplate restTemplate;

    public Location getLocation(UUID id) {
        try {
            return restTemplate.getForObject(serviceUrl(LOCATION_SERVICE) + GET_LOCATION_URI + id, Location.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new LocationNotFoundException(LOCATION_NOT_FOUND);
        }
    }
}
