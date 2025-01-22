package com.booking_app.booking_app.service;

import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.repository.BookingRepository;
import com.booking_app.booking_app.repository.MovieRepository;
import com.booking_app.booking_app.repository.ShowtimeRepository;
import com.booking_app.booking_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private BookingService bookingService;

    private final Movie movie = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);

    private Showtime getShowtime1() {
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setMovie(movie);
        showtime1.setTheater("Theater A");
        showtime1.setStartTime(LocalDateTime.now());
        showtime1.setEndTime(LocalDateTime.now().plusHours(2));
        return showtime1;
    }

    ;

    private Showtime getShowtime2() {
        Showtime showtime2 = new Showtime();
        showtime2.setId(2L);
        showtime2.setMovie(movie);
        showtime2.setTheater("Theater B");
        showtime2.setStartTime(LocalDateTime.now().plusHours(3));
        showtime2.setEndTime(LocalDateTime.now().plusHours(5));
        return showtime2;
    }

    ;

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Alon");
        return user;
    }

    private Booking getBooking() {
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
    void testBookTicketSuccessfully() {
        Booking booking = this.getBooking();

        when(userRepository.findByName(anyString())).thenReturn(Optional.of(this.getUser()));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(this.movie));
        when(showtimeRepository.findById(anyLong())).thenReturn(Optional.of(this.getShowtime2()));
        when(bookingRepository.existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt()))
                .thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.countByShowtime(any(Showtime.class))).thenReturn(-1);

        Booking result = bookingService.bookTicket(getUser().getName(), getShowtime1().getId(), movie.getTitle(), booking.getSeatNumber(), booking.getPrice());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        verify(showtimeRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt());
        verify(bookingRepository, times(1)).countByShowtime(any(Showtime.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testBookTicketThrowsExceptionWhenSeatAlreadyBooked() {
        Booking booking = this.getBooking();

        when(userRepository.findByName(anyString())).thenReturn(Optional.of(this.getUser()));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(this.movie));
        when(showtimeRepository.findById(anyLong())).thenReturn(Optional.of(this.getShowtime2()));
        when(bookingRepository.existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt()))
                .thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.bookTicket(getUser().getName(), getShowtime1().getId(), movie.getTitle(), booking.getSeatNumber(), booking.getPrice()));
        assertEquals("Seat 5 is already booked for this showtime", exception.getMessage());

        verify(bookingRepository, times(1)).existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testBookTicketThrowsExceptionWhenMaxSeatsReached() {
        Booking booking = this.getBooking();

        when(userRepository.findByName(anyString())).thenReturn(Optional.of(this.getUser()));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(this.movie));
        when(showtimeRepository.findById(anyLong())).thenReturn(Optional.of(this.getShowtime2()));
        when(bookingRepository.existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt()))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.bookTicket(getUser().getName(), getShowtime1().getId(), movie.getTitle(), booking.getSeatNumber(), booking.getPrice()));
        assertEquals("Maximum seat capacity reached for this showtime", exception.getMessage());

        verify(bookingRepository, times(1)).existsByShowtimeAndSeatNumber(any(Showtime.class), anyInt());
    }

    @Test
    void testGetBookingsByUser() {
        Long userId = 1L;
        List<Booking> bookings = Arrays.asList(
                this.getBooking()
        );
        when(bookingRepository.findByUserId(userId)).thenReturn(bookings);
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(this.getUser()));

        List<Booking> result = bookingService.getBookingsByUserName(this.getUser().getName());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByUserId(userId);
    }
}
