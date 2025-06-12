package com.ironhack.carrentalsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDrivingLicenseDTO {
    private Long userId;
    private String name;
    private String drivingLicenseNumber;
}
