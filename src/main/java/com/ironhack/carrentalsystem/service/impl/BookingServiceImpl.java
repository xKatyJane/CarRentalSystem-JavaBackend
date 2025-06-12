package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.dto.BookingRequestDTO;
import com.ironhack.carrentalsystem.dto.BookingResponseDTO;
import com.ironhack.carrentalsystem.dto.UpdateBookingDTO;
import com.ironhack.carrentalsystem.model.Booking;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.repository.BookingRepository;
import com.ironhack.carrentalsystem.repository.CarRepository;
import com.ironhack.carrentalsystem.repository.UserRepository;
import com.ironhack.carrentalsystem.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    // Create a booking
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        // Validate if start of the booking is not after its end
        if (request.getStartDateTime().isAfter(request.getEndDateTime())) {
            throw new RuntimeException("Start date must be before or equal to end date.");
        }
        // Prevent booking in the past
        if (request.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Booking cannot start in the past.");
        }
        // Validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        // Validate car
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + request.getCarId()));
        // Check car availability
        boolean isCarBooked = bookingRepository.existsByCarAndOverlappingDates(
                car.getId(), request.getStartDateTime(), request.getEndDateTime());
        if (isCarBooked) {
            throw new RuntimeException("Car is already booked for the selected dates.");
        }
        // Create and save booking
        Booking booking = new Booking();
        booking.setCustomer(user);
        booking.setCar(car);
        booking.setStartDateTime(request.getStartDateTime());
        booking.setEndDateTime(request.getEndDateTime());
        Booking savedBooking = bookingRepository.save(booking);
        // Calculate total rental price
        long totalHours = ChronoUnit.HOURS.between(request.getStartDateTime(), request.getEndDateTime());
        long totalDays = totalHours / 24;
        if (totalHours % 24 != 0) {
            totalDays++;
        }
        BigDecimal pricePerDay = car.getPricing().getPricePerDay();
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(totalDays));
        // Return response DTO
        return new BookingResponseDTO(
                savedBooking.getBookingId(),
                user.getId(),
                user.getUsername(),
                car.getId(),
                car.getPlateNumber(),
                car.getMake(),
                car.getModel(),
                pricePerDay,
                totalPrice,
                request.getStartDateTime(),
                request.getEndDateTime()
        );
    }

    // Edit an existing booking
    @Override
    @Transactional
    public void updateBooking(Long bookingId, UpdateBookingDTO dto) {
        if (dto.getCarId() == null && dto.getStartDateTime() == null && dto.getEndDateTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one field must be provided to update the booking.");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (dto.getCarId() != null) {
            Car car = carRepository.findById(dto.getCarId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
            booking.setCar(car);
        }
        if (dto.getStartDateTime() != null) {
            booking.setStartDateTime(dto.getStartDateTime());
        }
        if (dto.getEndDateTime() != null) {
            booking.setEndDateTime(dto.getEndDateTime());
        }
        bookingRepository.save(booking);
    }

    // Delete a booking by ID number
    @Override
    public void deleteBookingById(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Booking with ID " + bookingId + " not found.");
        }
        bookingRepository.deleteById(bookingId);
    }

    // See all bookings
    @Override
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(booking -> {
            User user = booking.getCustomer();
            Car car = booking.getCar();
            BigDecimal pricePerDay = car.getPricing().getPricePerDay();
            long hours = ChronoUnit.HOURS.between(booking.getStartDateTime(), booking.getEndDateTime());
            long days = hours / 24;
            if (hours % 24 != 0) days++; // Round up partial day
            BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));
            return new BookingResponseDTO(
                    booking.getBookingId(),
                    user.getId(),
                    user.getUsername(),
                    car.getId(),
                    car.getPlateNumber(),
                    car.getMake(),
                    car.getModel(),
                    pricePerDay,
                    totalPrice,
                    booking.getStartDateTime(),
                    booking.getEndDateTime()
            );
        }).collect(Collectors.toList());
    }

    // See a booking by ID number
    @Override
    public BookingResponseDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        User user = booking.getCustomer();
        Car car = booking.getCar();
        BigDecimal pricePerDay = car.getPricing().getPricePerDay();
        long totalHours = ChronoUnit.HOURS.between(booking.getStartDateTime(), booking.getEndDateTime());
        long totalDays = totalHours / 24;
        if (totalHours % 24 != 0) {
            totalDays++;
        }
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(totalDays));
        return new BookingResponseDTO(
                booking.getBookingId(),
                user.getId(),
                user.getUsername(),
                car.getId(),
                car.getPlateNumber(),
                car.getMake(),
                car.getModel(),
                pricePerDay,
                totalPrice,
                booking.getStartDateTime(),
                booking.getEndDateTime()
        );
    }

    // See all bookings of a user by user ID
    @Override
    public List<BookingResponseDTO> getBookingsByCustomerId(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        if (bookings.isEmpty()) {
            throw new RuntimeException("No bookings found for customer with id: " + customerId);
        }
        return bookings.stream().map(booking -> {
            User user = booking.getCustomer();
            Car car = booking.getCar();
            BigDecimal pricePerDay = car.getPricing().getPricePerDay();
            long hours = ChronoUnit.HOURS.between(booking.getStartDateTime(), booking.getEndDateTime());
            long days = hours / 24;
            if (hours % 24 != 0) {
                days++;
            }
            BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));
            return new BookingResponseDTO(
                    booking.getBookingId(),
                    user.getId(),
                    user.getUsername(),
                    car.getId(),
                    car.getPlateNumber(),
                    car.getMake(),
                    car.getModel(),
                    pricePerDay,
                    totalPrice,
                    booking.getStartDateTime(),
                    booking.getEndDateTime()
            );
        }).collect(Collectors.toList());
    }

    // View own bookings
    @Override
    public List<BookingResponseDTO> getOwnBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Booking> bookings = bookingRepository.findByCustomer(user);

        return bookings.stream().map(booking -> {
            Car car = booking.getCar();
            BigDecimal pricePerDay = car.getPricing().getPricePerDay();

            long totalHours = ChronoUnit.HOURS.between(booking.getStartDateTime(), booking.getEndDateTime());
            long totalDays = totalHours / 24;
            if (totalHours % 24 != 0) totalDays++;

            BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(totalDays));

            return new BookingResponseDTO(
                    booking.getBookingId(),
                    user.getId(),
                    user.getUsername(),
                    car.getId(),
                    car.getPlateNumber(),
                    car.getMake(),
                    car.getModel(),
                    pricePerDay,
                    totalPrice,
                    booking.getStartDateTime(),
                    booking.getEndDateTime()
            );
        }).collect(Collectors.toList());
    }
}