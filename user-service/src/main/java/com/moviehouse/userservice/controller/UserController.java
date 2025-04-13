package com.moviehouse.userservice.controller;


import com.moviehouse.userservice.dataaccess.model.User;
import com.moviehouse.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user-info-management/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Validated User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("user/{id}")
    public void deleteUserById(@PathVariable("id") UUID id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User login){
        User user = userService.login(login);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
