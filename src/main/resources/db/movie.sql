create table if not exists `booking-app-db`.movie
(
    id           bigint auto_increment
        primary key,
    duration     int          not null,
    genre        varchar(255) not null,
    rating       double       not null,
    release_year int          not null,
    title        varchar(255) not null
);