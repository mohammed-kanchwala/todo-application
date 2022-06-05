package com.mk.controller;

import com.mk.model.UserDto;
import com.mk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public ResponseEntity<?> showRegistrationForm(@RequestBody UserDto user) {
        userService.register(user);
        return ok("User Registration Successful !!");
    }
}
