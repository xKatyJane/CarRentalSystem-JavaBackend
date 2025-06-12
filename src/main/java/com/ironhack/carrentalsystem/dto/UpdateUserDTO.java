package com.ironhack.carrentalsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String email;
    private Integer telephoneNumber;
}
