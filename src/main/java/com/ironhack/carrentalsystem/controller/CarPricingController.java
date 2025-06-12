package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.UpdateCarPricingDTO;
import com.ironhack.carrentalsystem.service.CarPricingService;
import com.ironhack.carrentalsystem.service.impl.CarPricingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/cars/pricing")
@AllArgsConstructor
public class CarPricingController {
    private final CarPricingServiceImpl carPricingServiceImpl;
    private final CarPricingService carPricingService;

    @PutMapping()
    public ResponseEntity<?> updateCarPricing(@RequestBody UpdateCarPricingDTO dto) {
        try {
            carPricingService.updateCarPricing(dto);
            return ResponseEntity.ok(Map.of("message", "Car pricing updated successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
