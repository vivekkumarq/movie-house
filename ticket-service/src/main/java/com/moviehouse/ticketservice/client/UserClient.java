package com.moviehouse.ticketservice.client;

import com.moviehouse.ticketservice.dataaccess.model.Movie;
import com.moviehouse.ticketservice.dataaccess.model.User;
import com.moviehouse.ticketservice.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class UserClient extends ServiceDiscovery{
    private static final String GET_USER_URI = "/user-info-management/user/";
    private static final String USER_SERVICE = "user-service";

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(UUID id) {
        System.out.println(serviceUrl(USER_SERVICE));
        try {
            return restTemplate.getForObject(serviceUrl(USER_SERVICE) + GET_USER_URI + id, User.class);
//            return restTemplate.getForObject("http://ws-21290.netcracker.com:8088" + GET_USER_URI + id, User.class);
        }catch (RuntimeException e){
            throw new UserNotFound("User not found");
        }
    }

}
