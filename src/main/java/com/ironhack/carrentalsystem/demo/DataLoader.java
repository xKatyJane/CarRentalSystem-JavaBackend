package com.ironhack.carrentalsystem.demo;

import com.ironhack.carrentalsystem.model.*;
import com.ironhack.carrentalsystem.model.enums.CarCategory;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import com.ironhack.carrentalsystem.repository.*;
import com.opencsv.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final CarRepository carRepository;
    private final CarParametersRepository carParametersRepository;
    private final CarPricingRepository carPricingRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CarRepository carRepository,
                      CarParametersRepository carParametersRepository,
                      CarPricingRepository carPricingRepository,
                      UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.carRepository = carRepository;
        this.carParametersRepository = carParametersRepository;
        this.carPricingRepository = carPricingRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loadRolesAndUsers();

        if (carRepository.count() == 0) {
            loadCarsFromCsv("data/cars.csv");
        }
    }

    private void loadRolesAndUsers() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN", null));
            roleRepository.save(new Role(null, "USER", null));
            System.out.println("Default roles loaded");
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setEmail("admin@example.com");
            admin.setTelephoneNumber(123456789);
            admin.getRoles().add(adminRole);

            User user = new User();
            user.setName("User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setEmail("user@example.com");
            user.setTelephoneNumber(987654321);
            user.getRoles().add(userRole);

            userRepository.save(admin);
            userRepository.save(user);

            System.out.println("Default users loaded");
        }
    }

    private void loadCarsFromCsv(String filePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             InputStreamReader reader = new InputStreamReader(is);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] row;
            csvReader.readNext(); // Skip header

            while ((row = csvReader.readNext()) != null) {
                Car car = new Car();
                car.setPlateNumber(row[0]);
                car.setPetrolType(PetrolType.valueOf(row[1].toUpperCase()));
                car.setGearBoxType(GearBoxType.valueOf(row[2].toUpperCase()));
                car.setMake(row[3]);
                car.setModel(row[4]);
                car.setNumberOfSeats(Integer.parseInt(row[5]));

                CarPricing pricing = new CarPricing();
                pricing.setPricePerDay(new BigDecimal(row[6]));
                pricing.setCar(car);
                car.setPricing(pricing);

                CarParameters parameters = new CarParameters();
                parameters.setMileage(Integer.parseInt(row[7]));
                parameters.setNextInspectionDate(LocalDate.parse(row[8]));

                Set<CarCategory> categories = Arrays.stream(row[9].split("\\|"))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .map(CarCategory::valueOf)
                        .collect(Collectors.toSet());

                parameters.setCategories(categories);
                parameters.setCar(car);
                car.setParameters(parameters);

                carRepository.save(car); // Cascade saves pricing and parameters
            }

            System.out.println("Cars loaded from CSV.");

        } catch (Exception e) {
            System.err.println("Error loading cars from CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}