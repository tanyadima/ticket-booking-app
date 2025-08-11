package com.booking_app.booking_app.dto;

import java.time.LocalDateTime;

public record ShowtimeDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String movie,
        String theater
) {}


