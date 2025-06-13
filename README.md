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
### Cars
- GET api/cars
- GET api/cars/priceRange
- GET api/cars/available
- GET api/cars/id/{carId}
- GET api/cars/plate/{plateNumber}
- DELETE api/cars/{id}
- PUT api/carParameters/{carId}<br>&nbsp;&nbsp;&nbsp;&nbsp;{"mileage", "nextInspectionDate", "categories"}
- PUT api/pricing
        {"carId", "pricePerDay"}
  
### Roles
- GET api/roles
- POST api/roles
- DELETE api/roles
- PATCH api/roles/roleAssignment
         {"username", "role"}

- GET api/users
