package com.moviehouse.userservice.service;

import com.moviehouse.userservice.dataaccess.model.LoginRequest;
import com.moviehouse.userservice.dataaccess.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    User addUser(User user);
    User getUserById(UUID id);
    User getUserByUsername(String username);
    Page<User> getAllUsers(Pageable pageable);
    void deleteUserById(UUID id);
    User login(LoginRequest login);
}
