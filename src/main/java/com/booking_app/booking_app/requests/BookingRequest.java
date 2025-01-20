package com.booking_app.booking_app.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingRequest {
    private String userName;
    private Long showtimeId;
    private String movieTitle;
    private Integer seatNumber;
    private BigDecimal price;

    public String getUserName() {
        return userName;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
