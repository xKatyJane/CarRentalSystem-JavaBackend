package com.ironhack.carrentalsystem.mapper;

import com.ironhack.carrentalsystem.dto.UserDTO;
import com.ironhack.carrentalsystem.model.Role;
import com.ironhack.carrentalsystem.model.User;

import java.util.stream.Collectors;

public class UserToUserDTOMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()));
    }
}