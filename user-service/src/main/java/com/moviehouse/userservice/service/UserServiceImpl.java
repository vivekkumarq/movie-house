package com.moviehouse.userservice.service;

import com.moviehouse.userservice.client.LocationClient;
import com.moviehouse.userservice.dataaccess.model.User;
import com.moviehouse.userservice.exception.DuplicateUsernameException;
import com.moviehouse.userservice.exception.LocationNotFoundException;
import com.moviehouse.userservice.exception.UnauthorizedUserException;
import com.moviehouse.userservice.exception.UserNotFoundException;
import com.moviehouse.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USERNAME_OR_PASSWORD_INCORRECT = "Username or Password is incorrect";

    private static final String DUPLICATE_USERNAME = "User already exists with this username";
    private static final String LOCATION_NOT_FOUND = "Location not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationClient locationClient;


    @Override
    public User addUser(User user) {
        User userExists = getUserByUsername(user.getUsername());
        if(userExists!=null)
            throw new DuplicateUsernameException(DUPLICATE_USERNAME);
        if(locationClient.getLocation(user.getLocation().getId())==null)
            throw new LocationNotFoundException(LOCATION_NOT_FOUND);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public User login(User login) {
        User user = getUserByUsername(login.getUsername());
        if(user==null || !passwordEncoder.matches(login.getPassword(), user.getPassword()))
            throw new UnauthorizedUserException(USERNAME_OR_PASSWORD_INCORRECT);
        return user;
    }

}
