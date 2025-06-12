package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.RoleToUserDTO;
import com.ironhack.carrentalsystem.model.Role;
import com.ironhack.carrentalsystem.service.impl.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @GetMapping
    public List<Role> findAll() {
        return roleServiceImpl.findAll();
    }

    @PostMapping("/newRole")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> newRole(@RequestBody Role role) {
        try {
            roleServiceImpl.save(role);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(String.format("Successfully created a new role %s", role.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteRole")
    public ResponseEntity<String> deleteRole(@RequestBody Role role) {
        try {
            roleServiceImpl.deleteRole(role.getName());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Successfully deleted role %s", role.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/addRoleToUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addRole(@RequestBody RoleToUserDTO roleToUserDTO) {
        roleServiceImpl.addRoleToUser(roleToUserDTO.getUsername(), roleToUserDTO.getRole());
        return ResponseEntity.ok(String.format("Role %s added successfully to user %s", roleToUserDTO.getRole(), roleToUserDTO.getUsername()));
    }

    @PutMapping("/removeRoleFromUser")
    public ResponseEntity<String> removeRoleFromUser(@RequestBody RoleToUserDTO roleToUserDTO) {
        try {
            roleServiceImpl.removeRoleFromUser(roleToUserDTO.getUsername(), roleToUserDTO.getRole());
            return ResponseEntity.ok(
                    String.format("Successfully removed role '%s' from user '%s'", roleToUserDTO.getRole(), roleToUserDTO.getUsername()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
