package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.dto.CarAvailabilityDTO;
import com.ironhack.carrentalsystem.dto.CreateCarDTO;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.CarParameters;
import com.ironhack.carrentalsystem.model.CarPricing;
import com.ironhack.carrentalsystem.model.enums.CarCategory;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import com.ironhack.carrentalsystem.repository.BookingRepository;
import com.ironhack.carrentalsystem.repository.CarRepository;
import com.ironhack.carrentalsystem.service.CarService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    // Get all cars
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Get filtered cars
    public List<Car> getFilteredCars(CarCategory category,
                                     GearBoxType gearBoxType,
                                     PetrolType petrolType,
                                     String make) {
        return carRepository.findAll().stream()
                .filter(car -> gearBoxType == null || car.getGearBoxType() == gearBoxType)
                .filter(car -> petrolType == null || car.getPetrolType() == petrolType)
                .filter(car -> make == null || car.getMake().equalsIgnoreCase(make))
                .filter(car -> category == null ||
                        (car.getParameters() != null &&
                                car.getParameters().getCategories() != null &&
                                car.getParameters().getCategories().contains(category)))
                .collect(Collectors.toList());
    }

    // Get cars in a price range
    @Override
    public List<Car> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return carRepository.findByPriceRange(minPrice, maxPrice);
    }

    // Get car by plate number
    public Optional<Car> getCarByPlateNumber(String plateNumber) {
        return carRepository.getCarByPlateNumber(plateNumber);
    }

    //Get car by ID number
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Get all cars available on chosen dates
    @Override
    public List<CarAvailabilityDTO> getAvailableCars(LocalDateTime start, LocalDateTime end,
                                                      PetrolType petrolType, GearBoxType gearBoxType) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start datetime must be before end datetime");
        }
        List<Long> bookedCarIds = bookingRepository.findBookedCarIdsBetween(start, end);
        List<Car> availableCars = carRepository.findAll().stream()
                .filter(car -> !bookedCarIds.contains(car.getId()))
                .filter(car -> petrolType == null || car.getPetrolType() == petrolType)
                .filter(car -> gearBoxType == null || car.getGearBoxType() == gearBoxType)
                .toList();
        return availableCars.stream()
                .map(car -> new CarAvailabilityDTO(
                        car.getId(),
                        car.getModel(),
                        car.getMake(),
                        car.getPetrolType(),
                        car.getGearBoxType(),
                        car.getPricing().getPricePerDay()
                ))
                .toList();
    }

    // Add a new car
    @Override
    @Transactional
    public void addNewCar(CreateCarDTO dto) {
        if (carRepository.existsByPlateNumber((dto.getPlateNumber()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car with this plate already exists");
        }

        Car car = new Car();
        car.setPlateNumber(dto.getPlateNumber());
        car.setPetrolType(dto.getPetrolType());
        car.setGearBoxType(dto.getGearBoxType());
        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setNumberOfSeats(dto.getNumberOfSeats());

        CarParameters parameters = new CarParameters();
        parameters.setMileage(dto.getMileage());
        parameters.setNextInspectionDate(dto.getNextInspectionDate());
        parameters.setCategories(dto.getCategories());
        parameters.setCar(car);

        CarPricing pricing = new CarPricing();
        pricing.setPricePerDay(dto.getPricePerDay());
        pricing.setCar(car);

        car.setParameters(parameters);
        car.setPricing(pricing);
        carRepository.save(car);
    }

    // Delete a car by ID
    @Override
    public void deleteCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car with ID " + id + " not found"));
        carRepository.delete(car);
    }
}
