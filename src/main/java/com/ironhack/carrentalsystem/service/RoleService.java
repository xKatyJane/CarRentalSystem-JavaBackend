package com.ironhack.carrentalsystem.service;

import com.ironhack.carrentalsystem.model.Role;

import java.util.List;

public interface RoleService {

    // Display all existing roles
    List<Role> findAll();

    // Save a new role
    Role save(Role role);

    // Delete an existing role
    void deleteRole(String role);

    // Assign a role to a user
    void addRoleToUser(String username, String roleName);

    // Remove a role from a user
    void removeRoleFromUser(String username, String roleName);
}
