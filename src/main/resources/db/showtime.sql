create table if not exists `booking-app-db`.showtime
(
    id         bigint auto_increment
        primary key,
    end_time   datetime(6)  null,
    start_time datetime(6)  null,
    theater    varchar(255) null,
    movie_id   bigint       null,
    constraint FK8i90asti16tydhva795c3qwj2
        foreign key (movie_id) references `booking-app-db`.movie (id)
);