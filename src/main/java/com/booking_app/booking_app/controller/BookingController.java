package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.exceptions.BookingNotFoundException;
import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.requests.BookingRequest;
import com.booking_app.booking_app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/ticket")
    public ResponseEntity<Booking> bookTicket(@RequestBody BookingRequest request) {
        Booking booking = bookingService.bookTicket(
                request.getUserName(),
                request.getShowtimeId(),
                request.getMovieTitle(),
                request.getSeatNumber(),
                request.getPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Booking>> getBookingsByUserName(@PathVariable String userName ) {
        List<Booking> bookings = bookingService.getBookingsByUserName(userName);
        if(!bookings.isEmpty()){
            return ResponseEntity.ok(bookings);
        }
        throw new BookingNotFoundException("Booking not found");
    }
}
