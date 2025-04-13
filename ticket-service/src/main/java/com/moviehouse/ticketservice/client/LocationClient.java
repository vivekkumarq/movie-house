package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.Location;
import com.moviehouse.ticketservice.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class LocationClient extends ServiceDiscovery{
    private static final String GET_LOCATION_URI = "/location-info-management/location/";
    private static final String LOCATION_SERVICE = "location-service";

    @Autowired
    private RestTemplate restTemplate;

    public Location getLocation(UUID id) {
        System.out.println(serviceUrl(LOCATION_SERVICE));
        try {
            return restTemplate.getForObject(serviceUrl(LOCATION_SERVICE) + GET_LOCATION_URI + id, Location.class);
//            return restTemplate.getForObject("http://ws-21290.netcracker.com:8089" + GET_LOCATION_URI + id, Location.class);
        }catch (RuntimeException e){
            throw new LocationNotFoundException("Location not found");
        }
    }
}
