package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.security.JwtUtil;
import com.booking_app.booking_app.service.BookingService;
import com.booking_app.booking_app.service.CustomUserDetailsService;
import com.booking_app.booking_app.service.MovieService;
import com.booking_app.booking_app.service.ShowtimeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class})
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ShowtimeService showtimeService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Movie movie = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);
    private Showtime getShowtime1(){
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setMovie(movie);
        showtime1.setTheater("Theater A");
        showtime1.setStartTime(LocalDateTime.now());
        showtime1.setEndTime(LocalDateTime.now().plusHours(2));
        return showtime1;
    };
    private User getUser(){
        User user = new User();
        user.setId(1L);
        user.setName("Alon");
        return user;
    }
    private Booking getBooking(){
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPrice(BigDecimal.valueOf(55.50));
        booking.setShowtime(this.getShowtime1());
        booking.setMovie(movie);
        booking.setUser(this.getUser());
        booking.setSeatNumber(5);
        return booking;
    }


    @Test
    public void testBookTicket() throws Exception {
        Booking booking = this.getBooking();

        when(bookingService.bookTicket(Mockito.any(String.class),Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(Integer.class),Mockito.any(BigDecimal.class))).thenReturn(booking);

        mockMvc.perform(post("/book/ticket")
                        .with(SecurityMockMvcRequestPostProcessors.user("customer").roles("CUSTOMER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\": \"customer1\"," +
                                "\"movieTitle\": \"film\"," +
                                "\"showtimeId\": 1," +
                                "\"seatNumber\": 5," +
                                "\"price\": 12.5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber", is(5)));
    }

    @Test
    public void testGetTicketsForUser() throws Exception {
        Booking booking = this.getBooking();
        when(bookingService.getBookingsByUserName("customer1")).thenReturn(Arrays.asList(this.getBooking()));

        mockMvc.perform(get("/book/user/customer1")
                .with(SecurityMockMvcRequestPostProcessors.user("amin").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].seatNumber", is(5)));
    }

    @Test
    public void testGetTickets() throws Exception {
        Booking booking = this.getBooking();
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(this.getBooking()));

        mockMvc.perform(get("/book")
                        .with(SecurityMockMvcRequestPostProcessors.user("amin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].seatNumber", is(5)));
    }

    @Test
    public void fetchTicketsFailsWith401WhenSessionIsNotValid() throws Exception {
        mockMvc.perform(get("/book", 1L))
                .andExpect(status().isUnauthorized());
    }
}

