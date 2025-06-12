package com.ironhack.carrentalsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Car ID is required")
    private Long carId;
    @NotNull(message = "Start date and time is required. Format: yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime startDateTime;
    @NotNull(message = "End date and time is required. Format: yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime endDateTime;
}