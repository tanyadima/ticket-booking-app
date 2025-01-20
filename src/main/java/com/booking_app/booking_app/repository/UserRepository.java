package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    // Find user by name (used for authentication)
    Optional<User> findByName(String name);
}
