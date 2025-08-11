package com.booking_app.booking_app.dto;

import com.booking_app.booking_app.enums.Role;

public class UserRegistrationRequest {
    private String name;
    private String password;
    private String email;
    private String role;

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
