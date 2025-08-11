package com.booking_app.booking_app.dto;

import java.math.BigDecimal;

public record BookingDto(
        Long id,
        String userName,
        String movieTitle,
        Integer seatNumber,
        BigDecimal price
) {}