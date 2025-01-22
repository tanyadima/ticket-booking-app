# Movie Ticket Booking Application

## Overview
This application is a RESTful API-based movie ticket booking system built using Spring Boot. 
It manages movies, showtimes, users, and ticket bookings, ensuring robust authentication
and authorization mechanisms.

## Features

### 1. Movie Management
* Add Movies: Create new movies with details (title, genre, duration, rating, release year).
* Update Movies: Modify existing movie details.
* Delete Movies: Remove movies from the system.
* Fetch Movies: Retrieve a list of movies or specific movie details.
### 2. Showtime Management
* Add Showtimes: Create new showtimes with details (movie, theater, start time, end time).
* Update Showtimes: Modify existing showtime details.
* Delete Showtimes: Remove showtimes from the system.
* Fetch Showtimes: Retrieve all showtimes for a specific movie or theater.
### 3. User Management
* Register Users: Create new users with roles (Admin/Customer).
* Authentication: Login and logout functionalities.
* Authorization:
  Admins can manage movies and showtimes.
  Customers can book tickets.
### 4. Ticket Booking
* Book Tickets: Customers can book tickets for available showtimes.
* Booking Details: user, movie, showtime, seat number, and price.
* Seat Availability: Prevent double booking of seats for the same showtime.
* Constraints: Maximum seats per showtime are configurable in application.properties file.
* Fetch all bookings and bookings by userName: Retrieve all bookings and bookings for a specific user.
* Authorization:
    Admins can fetch bookings data.
    Customers can book tickets.

## Technologies Used
* Backend: Java, Spring Boot
* Database: MySQL
* Security: Spring Security (Role-based Authorization) using JWT
* Build Tool: Maven
* JWT (JSON Web Token)
* JUnit & Mockito
* OpenAPI

## Setup Instructions

### 1. Prerequisites
* Java Development Kit (JDK) 11+
* MySQL Database
* Maven

### 2. Clone the Repository
git clone https://github.com/tanyadima/ticket-booking-app.git
cd movie-ticket-booking

### 3. Configure MySQL
* Update the application.properties file with your MySQL credentials:
  spring.datasource.url=jdbc:mysql://localhost:3306/movie_booking
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=update
* Create a database named booking-app-db
  you can use attached docker-compose.yml file from deployment directory
* Run scripts to create tables: booking, movie, showtime, user from src/main/resources/db

### 4. Build and Run the Application
* Build the application:
  mvn clean install
* Run the application:
  mvn spring-boot:run

### 5. Access API Endpoints
* The application runs on http://localhost:8081 by default.
  Update port if needed in application.properties
  server.port=8081
* First, register users using Postman collection (register)
* Login using Postman collection (login)
* After sussessfull login copy token jwt from the response body
* For every request,except register and login, add Authorization header and paste the value from the previous step
  like this: Bearer {token jwt from the login request}
* Use postman collection attached to the project: postmanCollection.json
* Use tools like Postman or cURL to interact with the API (movies, showtimes, ticket bookings)

### 6. Use application.properties file containing local configuration in the path src/main/resources
spring.application.name=booking-app
spring.datasource.url=jdbc:mysql://localhost:3306/booking-app-db
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8081
logging.level.org.springframework.security=DEBUG
showtime.max-seats=10
jwt.privateKey=<your private key as one line string>
jwt.publicKey=<your public key as one line string>
jwt.expirationMs=3600000

JWT expiration currently defined on 60 minutes, 
but it is configurable in configuration property: jwt.expirationMs

it is possible to use public and private keys from the application.properties file provided in the project 
to run application,
but it is possible to generate keys yourself and add them to application.properties file
to do it, use the following commands:
* use openssl commands to generate private and public key
  openssl genrsa -out private_key.pem 2048
  openssl rsa -in private_key.pem -pubout -out public_key.pem
* use command 
  awk 'NF {sub(/\\n/, ""); printf "%s\\n",$0;}' public.pem
  to change the key to one line string and add it to properties file

## Constraints
* Authentication: All endpoints except login and register require authentication 
  using Authorization header and jwt received from the login request.
* Authorization: 
  movie, showtime adn fetch book data are restricted to users with the ROLE_ADMIN role.
  book ticket are restricted to users with the ROLE_CUSTOMER role.

## API Endpoints

### Login
* Login: POST /login (For everyone)

### Register
* Register new user: POST /register (For everyone)

### Movies
* Add Movie: POST /movie (Admin Only)
* Update Movie: PUT /movie/{id} (Admin Only)
* Delete Movie: DELETE /movie/{id} (Admin Only)
* Fetch Movies: GET /movie  (Admin Only)
* Fetch Movie by Title: GET /movie/title/{film title} (Admin Only)
* Fetch Movie by Genre: GET /movie/search?genre={genre} (Admin Only)
* Fetch Movie by ReleaseYear: GET /movie/search?releaseYear={releaseYear} (Admin Only)

### Showtimes
* Add Showtime: POST /showtime (Admin Only)
* Update Showtime: PUT /showtime/{id} (Admin Only)
* Delete Showtime: DELETE /showtime/{id} (Admin Only)
* Fetch Showtimes: GET /showtime (Admin Only)
* Fetch Showtimes by Movie: GET /showtime/movie?movieTitle={movieTitle} (Admin Only)
* Fetch Showtimes by Theater: GET /showtime/theater?theater={theater} (Admin Only)


### Bookings
* Book Ticket: POST /book/ticket (Customer Only)
* Fetch Bookings by User: GET /book/user/{userName} (Admin Only)
* Fetch Bookings: GET /book (Admin Only)

## API Responses
200 Susses
201 Entity created successfully
403 The user is not authorized to access the resource
401 The jwt is invalid or expired, missing or invalid Authorization header
400 Bad Request
404 Entity not found
500 Internal Server Error/ Unknown error

OpenAPI provided in the project openapi.yaml

### Scripts for creating SQL tables

## Booking
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

## Movie
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

## Showtime
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

 ## User
create table if not exists `booking-app-db`.user
(
id       bigint auto_increment
primary key,
email    varchar(255)               not null,
name     varchar(255)               not null,
password varchar(255)               not null,
role     enum ('ADMIN', 'CUSTOMER') not null
);

## Improvement for production
In production, I would recommend: 
* separate login functionality, register functionality and main application 
  functionality: book ticket, showtime, movies into microservices
* keep secure data in the Vault
* use external token issuer instead of jwt, like Auth0
* keep data in configuration file
* using error codes for every request instead of error messages

## Contributors
* Shklovsky Dmitry - [tanyadima@gmail.com]

