package com.ironhack.carrentalsystem.dto;

import com.ironhack.carrentalsystem.model.enums.CarCategory;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateCarDTO {
    @NotBlank
    private String plateNumber;
    @NotNull
    private PetrolType petrolType;
    @NotNull
    private GearBoxType gearBoxType;
    @NotBlank
    private String make;
    @NotBlank
    private String model;
    @Min(1)
    private int numberOfSeats;
    @Min(0)
    private int mileage;
    @NotNull
    private LocalDate nextInspectionDate;
    @NotEmpty
    private Set<CarCategory> categories;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerDay;
}