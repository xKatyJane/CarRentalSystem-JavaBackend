package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.DrivingLicenseRequestDTO;
import com.ironhack.carrentalsystem.dto.UserDrivingLicenseDTO;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.model.enums.DrivingLicenseStatus;
import com.ironhack.carrentalsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/driving-licenses")
@RequiredArgsConstructor
public class DrivingLicenseApprovalController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> submitLicense(Principal principal,
                                           @RequestBody DrivingLicenseRequestDTO dto) {
        User user = userService.getUserByUsername(principal.getName());
        userService.submitDrivingLicense(user.getId(), dto.getDrivingLicenseNumber());
        return ResponseEntity.ok("License submitted for review");
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDrivingLicenseDTO>> getSubmittedLicenses() {
        List<User> pendingUsers = userService.getUsersByLicenseStatus(DrivingLicenseStatus.PENDING);
        List<UserDrivingLicenseDTO> dtos = pendingUsers.stream()
                .map(user -> new UserDrivingLicenseDTO(user.getId(), user.getName(), user.getDrivingLicenseNumber()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/admin/{id}/approve")
    public ResponseEntity<?> approveLicense(@PathVariable Long id) {
        userService.approveDrivingLicense(id);
        return ResponseEntity.ok("License approved");
    }

    @PostMapping("/admin/{id}/reject")
    public ResponseEntity<?> rejectLicense(@PathVariable Long id) {
        userService.rejectDrivingLicense(id);
        return ResponseEntity.ok("License rejected");
    }
}
