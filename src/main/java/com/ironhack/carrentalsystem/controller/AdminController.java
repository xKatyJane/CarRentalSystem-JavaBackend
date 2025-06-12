package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.UserDrivingLicenseDTO;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.model.enums.DrivingLicenseStatus;
import com.ironhack.carrentalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

/*    @Autowired
    private UserService userService;

    @GetMapping("/driving-licenses")
    public ResponseEntity<List<UserDrivingLicenseDTO>> getSubmittedLicenses() {
        List<User> pendingUsers = userService.getUsersByLicenseStatus(DrivingLicenseStatus.PENDING);

        // Map User entities to a DTO with only needed fields, like id, name, licenseNumber
        List<UserDrivingLicenseDTO> dtos = pendingUsers.stream()
                .map(user -> new UserDrivingLicenseDTO(user.getId(), user.getName(), user.getDrivingLicenseNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }*/
}