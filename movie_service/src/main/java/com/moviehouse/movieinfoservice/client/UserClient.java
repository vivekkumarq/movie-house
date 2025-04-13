package com.moviehouse.movieinfoservice.client;

import com.moviehouse.movieinfoservice.dataaccess.model.User;
import com.moviehouse.movieinfoservice.exception.UserNotFoundException;
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
//            return restTemplate.getForObject(serviceUrl(USER_SERVICE) + GET_USER_URI + id, User.class);
            return restTemplate.getForObject("http://ws-21290.netcracker.com:8088" + GET_USER_URI + id, User.class);
        }catch (Exception e){
            throw new UserNotFoundException("User not found");
        }
    }
}
