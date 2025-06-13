package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.CarParametersUpdateDTO;
import com.ironhack.carrentalsystem.model.CarParameters;
import com.ironhack.carrentalsystem.service.CarParametersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/carParameters")
@AllArgsConstructor
public class CarParametersController {
    private final CarParametersService carParametersService;

    @PatchMapping("/{carId}")
    public ResponseEntity<?> updateCarParameters(
            @PathVariable Long carId,
            @RequestBody CarParametersUpdateDTO dto
    ) {
        try {
            CarParameters updated = carParametersService.updateCarParameters(carId, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
