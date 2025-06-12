package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.CarAvailabilityDTO;
import com.ironhack.carrentalsystem.dto.CreateCarDTO;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.enums.CarCategory;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import com.ironhack.carrentalsystem.service.CarService;
import com.ironhack.carrentalsystem.service.impl.CarServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/cars")
@AllArgsConstructor
public class CarController {
    private final CarServiceImpl carServiceImpl;
    private final CarService carService;

    @GetMapping
    public List<Car> getAllCars(
            @RequestParam(required = false) CarCategory category,
            @RequestParam(required = false) GearBoxType gearBoxType,
            @RequestParam(required = false) PetrolType petrolType,
            @RequestParam(required = false) String make
            ) {
        return carServiceImpl.getFilteredCars(category, gearBoxType, petrolType, make);
    }

    @GetMapping("/priceRange")
    public ResponseEntity<?> getCarsByPriceRange(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        if (minPrice == null && maxPrice == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Enter at least one parameter: minPrice or maxPrice")
            );
        }
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        List<Car> cars = carService.getCarsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<?> getCarByPlateNumber(@PathVariable String plateNumber) {
        Optional<Car> carOptional = carServiceImpl.getCarByPlateNumber(plateNumber);
        if (carOptional.isPresent()) {
            return ResponseEntity.ok(carOptional.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("Invalid parameter", "No car with this plate number found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Optional<Car> carOptional = carServiceImpl.getCarById(id);
        if (carOptional.isPresent()) {
            return ResponseEntity.ok(carOptional.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("Invalid parameter", "No car with this ID number found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCars(
            @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(required = false) PetrolType petrolType,
            @RequestParam(required = false) GearBoxType gearBoxType) {
        try {
            List<CarAvailabilityDTO> availableCars = carService.getAvailableCars(startDateTime, endDateTime, petrolType, gearBoxType);
            return ResponseEntity.ok(availableCars);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<String> addCar(@RequestBody @Valid CreateCarDTO createCarDTO) {
        carService.addNewCar(createCarDTO);
        return ResponseEntity.ok("New car added successfully");
    }

    @DeleteMapping("/deleteCar/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCarById(id);
            return ResponseEntity.ok(Map.of("message", "Car with ID " + id + " was deleted successfully."));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }
}
