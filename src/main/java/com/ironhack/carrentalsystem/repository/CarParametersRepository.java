package com.ironhack.carrentalsystem.repository;

import com.ironhack.carrentalsystem.model.CarParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarParametersRepository extends JpaRepository<CarParameters, Long> {
}
