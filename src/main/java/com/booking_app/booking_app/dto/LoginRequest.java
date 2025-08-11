package com.booking_app.booking_app.dto;

public record LoginRequest(String username, String password) {
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}

