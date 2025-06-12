package com.ironhack.carrentalsystem.dto;

import com.ironhack.carrentalsystem.model.enums.CarCategory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Data
public class CarParametersUpdateDTO {
    private Integer mileage; // Optional
    private LocalDate nextInspectionDate; // Optional
    private Set<CarCategory> categories; // Optional
}
