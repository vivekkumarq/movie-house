package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.User;
import com.moviehouse.ticketservice.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class UserClient extends ServiceDiscovery {

    private static final String GET_USER_URI = "/user-info-management/user/";
    private static final String USER_SERVICE = "user-service";
    private static final String USER_NOT_FOUND = "User not found";

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(UUID id) {
        try {
            return restTemplate.getForObject(serviceUrl(USER_SERVICE) + GET_USER_URI + id, User.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFound(USER_NOT_FOUND);
        }
    }
}
