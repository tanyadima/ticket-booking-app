package com.booking_app.booking_app.service;

import com.booking_app.booking_app.enums.Role;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testAddMovie() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setName("test");
        user.setPassword("password");
        user.setRole(Role.valueOf("ADMIN"));

        when(userRepository.existsByName(user.getName())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        userService.registerUser(user.getName(), user.getEmail(), anyString(), user.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).existsByName(user.getName());
    }
}
