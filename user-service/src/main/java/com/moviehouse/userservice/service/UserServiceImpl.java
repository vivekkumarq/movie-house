package com.moviehouse.userservice.service;

import com.moviehouse.userservice.client.LocationClient;
import com.moviehouse.userservice.dataaccess.model.LoginRequest;
import com.moviehouse.userservice.dataaccess.model.User;
import com.moviehouse.userservice.exception.DuplicateUsernameException;
import com.moviehouse.userservice.exception.LocationNotFoundException;
import com.moviehouse.userservice.exception.UnauthorizedUserException;
import com.moviehouse.userservice.exception.UserNotFoundException;
import com.moviehouse.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

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
    @Transactional
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException(DUPLICATE_USERNAME);
        }
        if (locationClient.getLocation(user.getLocation().getId()) == null) {
            throw new LocationNotFoundException(LOCATION_NOT_FOUND);
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User login(LoginRequest login) {
        return userRepository.findByUsername(login.getUsername())
                .filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
                .orElseThrow(() -> new UnauthorizedUserException(USERNAME_OR_PASSWORD_INCORRECT));
    }
}
