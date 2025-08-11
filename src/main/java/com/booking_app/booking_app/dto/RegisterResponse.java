package com.booking_app.booking_app.dto;

public record RegisterResponse(String message, String username) {
    public String getMessage() {
        return message;
    }
    public String getUsername() {
        return username;
    }
}

