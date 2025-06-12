package com.ironhack.carrentalsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateCarPricingDTO {
    private Long carId;
    private BigDecimal pricePerDay;
}