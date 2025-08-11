package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.dto.BookingDto;
import com.booking_app.booking_app.dto.ShowtimeDto;
import com.booking_app.booking_app.exceptions.BookingNotFoundException;
import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.dto.BookingRequest;
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
    public ResponseEntity<BookingDto> bookTicket(@RequestBody BookingRequest request) {
        Booking booking = bookingService.bookTicket(
                request.getUserName(),
                request.getShowtimeId(),
                request.getMovieTitle(),
                request.getSeatNumber(),
                request.getPrice()
        );
        BookingDto bookingDto = new BookingDto(booking.getId(), booking.getUser().getName(), booking.getMovie().getTitle(), booking.getSeatNumber(), booking.getPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingDto> bookingDtos = bookings.stream()
                .map(booking -> new BookingDto(
                        booking.getId(),
                        booking.getUser().getName(),
                        booking.getMovie().getTitle(),
                        booking.getSeatNumber(),
                        booking.getPrice()
                ))
                .toList();
        return bookingDtos;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userName}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserName(@PathVariable String userName ) {
        List<Booking> bookings = bookingService.getBookingsByUserName(userName);
        if(!bookings.isEmpty()){
            List<BookingDto> bookingDtos = bookings.stream()
                    .map(booking -> new BookingDto(
                            booking.getId(),
                            booking.getUser().getName(),
                            booking.getMovie().getTitle(),
                            booking.getSeatNumber(),
                            booking.getPrice()
                    ))
                    .toList();
            return ResponseEntity.ok(bookingDtos);
        }
        throw new BookingNotFoundException("Booking not found");
    }
}
