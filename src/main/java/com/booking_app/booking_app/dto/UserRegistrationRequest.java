package com.booking_app.booking_app.dto;

import com.booking_app.booking_app.enums.Role;


public record UserRegistrationRequest(String name, String password, String  email, String role) {
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return Role.valueOf(role);
    }
}

