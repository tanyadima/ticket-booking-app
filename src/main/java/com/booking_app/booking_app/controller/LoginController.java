package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.dto.JwtResponse;
import com.booking_app.booking_app.dto.LoginRequest;
import com.booking_app.booking_app.exceptions.InvalidCredentialsException;
import com.booking_app.booking_app.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private String tokenType = "Bearer";

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            var user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String role = user.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(authRequest.getUsername(), role);
            return new JwtResponse(token, tokenType);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}