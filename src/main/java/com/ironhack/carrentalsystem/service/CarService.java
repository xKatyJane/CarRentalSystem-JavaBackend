package com.ironhack.carrentalsystem.service;

import com.ironhack.carrentalsystem.dto.CarAvailabilityDTO;
import com.ironhack.carrentalsystem.dto.CreateCarDTO;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.enums.CarCategory;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarService {


    // Get filtered cars
    List<Car> getFilteredCars(CarCategory category,
                              GearBoxType gearBoxType,
                              PetrolType petrolType,
                              String make);

    // Get cars in a price range
    List<Car> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // Get all available cars
    List<CarAvailabilityDTO> getAvailableCars(LocalDateTime start, LocalDateTime end,
                                              PetrolType petrolType, GearBoxType gearBoxType);

    // Admin endpoints
    Optional<Car> getCarByPlateNumber(String plateNumber);
    Optional<Car> getCarById(Long id);
    void addNewCar(CreateCarDTO createCarDTO);
    void deleteCarById(Long id);

}
