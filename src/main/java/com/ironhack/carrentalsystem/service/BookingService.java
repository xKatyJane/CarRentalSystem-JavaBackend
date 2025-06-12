package com.ironhack.carrentalsystem.service;

import com.ironhack.carrentalsystem.dto.BookingRequestDTO;
import com.ironhack.carrentalsystem.dto.BookingResponseDTO;
import com.ironhack.carrentalsystem.dto.UpdateBookingDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request);

    void updateBooking(Long bookingId, UpdateBookingDTO updateBookingDTO);

    void deleteBookingById(Long bookingId);

    List<BookingResponseDTO> getAllBookings();

    BookingResponseDTO getBookingById(Long bookingId);

    List<BookingResponseDTO> getBookingsByCustomerId(Long customerId);

    List<BookingResponseDTO> getOwnBookings(String username);
}
