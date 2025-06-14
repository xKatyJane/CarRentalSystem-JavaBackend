# Car Rental System API

This is a **backend-only RESTful API**, prepared as a final project for the Ironhack Java Backend development bootcamp, completed between March and June 2025.
This Car Rental System has been developed using **Java**, **Spring Boot**, **JPA**, and **MySQL**. It provides endpoints to manage cars, users, roles and bookings with full CRUD functionality via standard HTTP methods.


## Tech Stack

<li>Java</li>
<li>Maven</li>
<li>Spring Boot</li>
<li>Spring Data JPA</li>
<li>SQL (MySQL)</li>

## Features

<ul>
  <li>Car inventory management</li>
  <li>User registration</li>
  <li>Authentication and Authorization
    <ul>
      <li>Bearer Token Authentication using JWT</li>
      <li>Role-based access control (Admin and Secondary User)</li>
    </ul>
  </li>
  <li>RESTful endpoints with GET, POST, PUT, PATCH, DELETE</li>
  <li>Uses JPA for database access</li>
</ul>

## Diagram

![Diagram](https://raw.githubusercontent.com/xKatyJane/CarRentalSystem-JavaBackend/master/assets/Car_rental_diagram.png)

## Methodology
Initially the app is loaded with two roles: USER and ADMIN. The endpoints are authenticated using JWT Tokens. The endpoints: "/api/login", "/api/cars", "/api/cars/priceRange" and "/api/cars/available" have open access. /api/users/registration is available only for unauthenticated users: it allows them to register and obtain the credentials.<br><br>Authenticated endpoints include: GET "/api/users/me" and GET "/api/bookings/myBookings": these endpoints allow for an authenticated user to see their own data and bookings, respectively. This prevents regular users from seeing other user's data, as they can only access their own. Similarly, PATCH "/api/users/me" allows authenticated users to update their own data. POST "/api/bookings/newBooking" allows the authenticated users to create a new booking, and by using POST "/api/driving-licenses" these users can submit their driving license number for admin's approval.<br><br>All the remaining endpoints are admin endpoints. Admin functions include creating new roles, users, cars, as well as update operations: car pricing and car parameters updates. All the DELETE endpoints are admin only.

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
- PATCH api/carParameters/{carId}<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"mileage", "nextInspectionDate", "categories"}
- PUT api/pricing<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"carId", "pricePerDay"}
  
### Roles
- GET api/roles
- POST api/roles<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"name"}
- DELETE api/roles<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"name"}
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
- POST api/bookings<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"userId", "carId", "startDateTime", "endDateTime"}
- PATCH api/bookings/{id}
- DELETE api/bookings/{bookingId}

### Driving license approval
- POST api/driving-licenses
- GET api/driving-licenses/admin
- POST api/driving-licenses/admin/{requestId}/approve
- POST api/driving-licenses/admin/{requestId}/reject
- 

## Link to the presentation about the project:
https://docs.google.com/presentation/d/1uP-APjIxsNecglXtsgFtfzxdviEmOTCMMr_zoG4sONc/edit?slide=id.g3681e9d3af7_0_75#slide=id.g3681e9d3af7_0_75
