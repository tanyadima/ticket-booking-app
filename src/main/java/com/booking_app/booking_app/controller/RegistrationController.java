package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.dto.RegisterResponse;
import com.booking_app.booking_app.dto.UserRegistrationRequest;
import com.booking_app.booking_app.exceptions.DuplicateUserException;
import com.booking_app.booking_app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            userService.registerUser(request.getName(), request.getPassword(), request.getEmail(), request.getRole());
            RegisterResponse response = new RegisterResponse("User registered successfully", request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            throw new DuplicateUserException(ex.getMessage());
        }
    }
}


