package com.ironhack.carrentalsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.carrentalsystem.model.enums.GearBoxType;
import com.ironhack.carrentalsystem.model.enums.PetrolType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String plateNumber;
    private PetrolType petrolType;
    private GearBoxType gearBoxType;
    private String make;
    private String model;
    private Integer NumberOfSeats;
    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonManagedReference
    private CarPricing pricing;
    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonManagedReference
    private CarParameters parameters;
}
