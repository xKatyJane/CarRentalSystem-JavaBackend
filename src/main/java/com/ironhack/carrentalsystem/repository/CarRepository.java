package com.ironhack.carrentalsystem.repository;

import com.ironhack.carrentalsystem.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> getCarByPlateNumber(String plateNumber);
//    Optional<Car> getCarById(Long id);

    // Query to get cars in a price range
    @Query("SELECT c FROM Car c WHERE c.pricing.pricePerDay BETWEEN :minPrice AND :maxPrice")
    List<Car> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice);

    boolean existsByPlateNumber(String plateNumber);

//    List<Car> findByIdNotIn(Collection<Long> ids);
//    List<Car> findByIdNotIn(List<Long> ids);
}
