package com.ironhack.carrentalsystem.service;

import com.ironhack.carrentalsystem.dto.CarParametersUpdateDTO;
import com.ironhack.carrentalsystem.model.CarParameters;

public interface CarParametersService {

    CarParameters updateCarParameters(Long carId, CarParametersUpdateDTO dto);
}
