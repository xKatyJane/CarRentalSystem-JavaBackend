package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.BookingRequestDTO;
import com.ironhack.carrentalsystem.dto.BookingResponseDTO;
import com.ironhack.carrentalsystem.dto.BookingSuccessResponse;
import com.ironhack.carrentalsystem.dto.UpdateBookingDTO;
import com.ironhack.carrentalsystem.service.BookingService;
import com.ironhack.carrentalsystem.service.UserService;
import com.ironhack.carrentalsystem.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/newBooking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        try {
            BookingResponseDTO response = bookingService.createBooking(request);
            BookingSuccessResponse successResponse = new BookingSuccessResponse("Booking successful", response);
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<String> updateBooking(@PathVariable Long bookingId,
                                                @RequestBody UpdateBookingDTO dto) {
        bookingService.updateBooking(bookingId, dto);
        return ResponseEntity.ok("Booking updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBookingById(id);
            return ResponseEntity.ok(Map.of("message", "Booking deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/bookingById/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDTO booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getBookingsByCustomerId(@PathVariable Long customerId) {
        try {
            List<BookingResponseDTO> bookings = bookingService.getBookingsByCustomerId(customerId);
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingResponseDTO>> getOwnBookings(Principal principal) {
        List<BookingResponseDTO> bookings = bookingService.getOwnBookings(principal.getName());
        return ResponseEntity.ok(bookings);
    }
}