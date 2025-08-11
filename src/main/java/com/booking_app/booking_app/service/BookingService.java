package com.booking_app.booking_app.service;

import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.model.User;
import com.booking_app.booking_app.repository.BookingRepository;
import com.booking_app.booking_app.repository.MovieRepository;
import com.booking_app.booking_app.repository.ShowtimeRepository;
import com.booking_app.booking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Value("${showtime.max-seats}")
    private int maxSeatsPerShowtime;

    @Transactional
    public Booking bookTicket(String userName, Long showtimeId, String title, Integer seatNumber, BigDecimal price) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new DataIntegrityViolationException("User not found"));
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new DataIntegrityViolationException("Showtime not found"));
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new DataIntegrityViolationException("Movie not found"));
        if (!showtime.getMovie().getTitle().equals(title)) {
            throw new DataIntegrityViolationException("Showtime movie title does not match the provided movie title");
        }

        // Check if the seat is already booked
        if (bookingRepository.existsByShowtimeAndSeatNumber(showtime, seatNumber)) {
            throw new DataIntegrityViolationException("Seat " + seatNumber + " is already booked for this showtime");
        }

        // Check if maximum seats are reached
        Integer bookedSeats = bookingRepository.countByShowtime(showtime);
        if (bookedSeats >= maxSeatsPerShowtime) {
            throw new DataIntegrityViolationException("Maximum seat capacity reached for this showtime");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMovie(movie);
        booking.setShowtime(showtime);
        booking.setSeatNumber(seatNumber);
        booking.setPrice(price);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserName(String userName) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new DataIntegrityViolationException("User not found"));
        return bookingRepository.findByUserId(user.getId());
    }


}
