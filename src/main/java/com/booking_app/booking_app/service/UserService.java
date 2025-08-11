package com.booking_app.booking_app.service;

import com.booking_app.booking_app.enums.Role;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, String password, String email, Role role) {
        if (userRepository.existsByName(name)) {
            throw new RuntimeException(String.format("User with the name [%s] already exists", name));
        }

        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password)); // Encode password
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
    }
}
