package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.dto.CarParametersUpdateDTO;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.CarParameters;
import com.ironhack.carrentalsystem.repository.CarParametersRepository;
import com.ironhack.carrentalsystem.repository.CarRepository;
import com.ironhack.carrentalsystem.service.CarParametersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarParametersServiceImpl implements CarParametersService {

    private final CarRepository carRepository;
    private final CarParametersRepository carParametersRepository;

    // Update car parameters
    @Override
    public CarParameters updateCarParameters(Long carId, CarParametersUpdateDTO dto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + carId));
        CarParameters parameters = car.getParameters();
        if (parameters == null) {
            throw new RuntimeException("Car parameters not found for car ID: " + carId);
        }
        if (dto.getMileage() != null) {
            parameters.setMileage(dto.getMileage());
        }
        if (dto.getNextInspectionDate() != null) {
            parameters.setNextInspectionDate(dto.getNextInspectionDate());
        }
        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            parameters.setCategories(dto.getCategories());
        }
        return carParametersRepository.save(parameters);
    }
}
