package com.ironhack.carrentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingSuccessResponse {
    private String message;
    private BookingResponseDTO booking;
}
