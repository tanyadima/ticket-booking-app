create table if not exists `booking-app-db`.booking
(
    id          bigint auto_increment
        primary key,
    user_id     bigint         not null,
    showtime_id bigint         not null,
    movie_id    bigint         not null,
    seat_number int            not null,
    price       decimal(10, 2) not null,
    constraint showtime_id
        unique (showtime_id, seat_number),
    constraint booking_ibfk_1
        foreign key (movie_id) references `booking-app-db`.movie (id),
    constraint booking_ibfk_2
        foreign key (user_id) references `booking-app-db`.user (id),
    constraint booking_ibfk_3
        foreign key (showtime_id) references `booking-app-db`.showtime (id)
);