package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.enums.Role;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.repository.UserRepository;
import com.booking_app.booking_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RegistrationController.class})
public class RegisterControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;



    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setName("Alon");
        user.setEmail("test@gmail.com");
        user.setPassword("pass");
        user.setRole(Role.valueOf("ADMIN"));
        doNothing().when(userService).registerUser(anyString(), anyString(), anyString(), any());
        when(userRepository.existsByName("Alon")).thenReturn(true);
        mockMvc.perform(post("/register")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\": \"name\"," +
                                "\"password\": \"pass\"," +
                                "\"email\": \"test@gmail.com\"," +
                                "\"role\": \"ADMIN\"}"))
                .andExpect(status().isOk());
    }
}

