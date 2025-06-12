package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.dto.UpdateCarPricingDTO;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.CarPricing;
import com.ironhack.carrentalsystem.repository.CarPricingRepository;
import com.ironhack.carrentalsystem.repository.CarRepository;
import com.ironhack.carrentalsystem.service.CarPricingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Getter
@Setter
@Service
public class CarPricingServiceImpl implements CarPricingService {
    private final CarPricingRepository carPricingRepository;
    private final CarRepository carRepository;

    // Update car pricing
    @Override
    public void updateCarPricing(UpdateCarPricingDTO dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + dto.getCarId()));

        CarPricing pricing = car.getPricing();
        if (pricing == null) {
            pricing = new CarPricing();
            pricing.setCar(car);
        }
        pricing.setPricePerDay(dto.getPricePerDay());

        carPricingRepository.save(pricing);
    }
}
