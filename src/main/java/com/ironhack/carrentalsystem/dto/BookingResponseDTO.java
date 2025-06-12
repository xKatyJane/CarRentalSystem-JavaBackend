package com.ironhack.carrentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;
    private Long userId;
    private String username;
    private Long carId;
    private String plateNumber;
    private String make;
    private String model;
    private BigDecimal pricePerDay;
    private BigDecimal totalPrice;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
