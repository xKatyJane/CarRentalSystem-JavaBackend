package com.ironhack.carrentalsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.carrentalsystem.model.enums.CarCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="car_parameters")
public class CarParameters {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int mileage;
    private LocalDate nextInspectionDate;
    @ElementCollection(targetClass = CarCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="car_categories", joinColumns = @JoinColumn(name="car_id"))
    @Column(name="category")
    private Set<CarCategory> categories = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @JsonBackReference
    private Car car;
}