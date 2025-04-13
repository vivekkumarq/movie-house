package com.moviehouse.userservice.service;

import com.moviehouse.userservice.dataaccess.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User addUser(User user);
    User getUserById(UUID id);
    User getUserByUsername(String username);
    List<User> getAllUsers();

    void deleteUserById(UUID id);

    User login(User login);
}
