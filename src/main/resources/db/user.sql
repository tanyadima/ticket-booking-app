create table if not exists `booking-app-db`.user
(
    id       bigint auto_increment
        primary key,
    email    varchar(255)               not null,
    name     varchar(255)               not null,
    password varchar(255)               not null,
    role     enum ('ADMIN', 'CUSTOMER') not null
);