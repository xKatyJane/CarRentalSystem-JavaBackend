#Car Rental System API  🚗

This is a **backend-only RESTful API**, prepared as a final project for the Ironhack Java Backend development bootcamp.
This Car Rental System has been developed using **Java**, **Spring Boot**, **JPA**, and **MySQL**. It provides endpoints to manage cars, users, roles and bookings with full CRUD functionality via standard HTTP methods.



## Tech Stack

- Java
- Maven
- Spring Boot
- Spring Data JPA (Hibernate)
- SQL (MySQL)


## Features

- Car inventory management
- Customer registration and management
- Rental booking and return system
- RESTful endpoints with GET, POST, PUT, PATCH, DELETE
- Uses JPA for database access

## Diagram

![Diagram](https://raw.githubusercontent.com/xKatyJane/CarRentalSystem-JavaBackend/master/assets/Car_rental_diagram.png)

## Endpoints

### Login
- GET api/login

### Cars
- GET api/cars
- GET api/cars/priceRange
- GET api/cars/available
- GET api/cars/id/{carId}
- GET api/cars/plate/{plateNumber}
- POST api/cars<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"plateNumber", "petrolType", "gearBoxType", "make", "model", "numberOfSeats", "mileage", "nextInspectionDate", "categories", "pricePerDay"}
- DELETE api/cars/{id}
- PUT api/carParameters/{carId}<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"mileage", "nextInspectionDate", "categories"}
- PUT api/pricing<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"carId", "pricePerDay"}
  
### Roles
- GET api/roles
- POST api/roles
- DELETE api/roles
- PATCH api/roles/roleAssignment<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"username", "role"}

### Users
- GET api/users
- GET api/users/me
- POST api/users/registration
- POST api/users/newUser
- PATCH api/users/me
- DELETE api/users/{username}

### Bookings
- GET api/bookings
- GET api/bookings/{bookingId}
- GET api/bookings/customer/{customerId}
- GET api/bookings/myBookings
- POST api/bookings
- PATCH api/bookings/{id}
- DELETE api/bookings/{bookingId}

### Driving license approval
- POST api/driving-licenses
- GET api/driving-licenses/admin
- POST api/driving-licenses/admin/{requestId}/approve
- POST api/driving-licenses/admin/{requestId}/reject
