package com.ironhack.carrentalsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateBookingDTO {
    private Long carId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}