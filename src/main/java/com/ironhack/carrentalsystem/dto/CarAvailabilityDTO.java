package com.ironhack.carrentalsystem.dto;

import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CarAvailabilityDTO {
    private Long carId;
    private String make;
    private String model;
    private PetrolType petrolType;
    private GearBoxType gearBoxType;
    private BigDecimal pricePerDay;
}