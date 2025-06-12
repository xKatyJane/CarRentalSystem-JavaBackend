package com.ironhack.carrentalsystem.repository;

import com.ironhack.carrentalsystem.model.CarPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPricingRepository extends JpaRepository<CarPricing, Long> {
}
