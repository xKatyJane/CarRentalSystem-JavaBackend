package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.model.Role;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.repository.RoleRepository;
import com.ironhack.carrentalsystem.repository.UserRepository;
import com.ironhack.carrentalsystem.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    // Display all existing roles
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    // Save a new role
    @Override
    public Role save(Role role) {
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            throw new RuntimeException("Role '" + role.getName() + "' already exists.");
        } else {
            log.info("Saved new role {} to the database", role.getName());
            return roleRepository.save(role);
        }
    }

    // Delete an existing role
    @Override
    @Transactional
    public void deleteRole(String roleName) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        // If a role doesn't exist show a message "NOT FOUND"
        if (existingRole.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "\n" + "Role '" + roleName + "' does not exist."
            );
        } else {
            // Check if a role is assigned to any users, and if yes, remove it from these users
            Role roleToDelete = existingRole.get();
            List<User> usersWithRole = userRepository.findByRoles_Name((roleName));
            for (User user : usersWithRole) {
                user.getRoles().remove(roleToDelete);
            }
            userRepository.saveAll(usersWithRole);
            // Delete role
            roleRepository.delete(roleToDelete);
            log.info("Deleted role {} from the database", roleName);
        }
    }

    // Assign a role to a user
    @Override
    public void addRoleToUser(String username, String roleName) {
        // If user doesn't exist display an error message
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User '" + username + "' not found"));
        // If role doesn't exist display an error message
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role '" + roleName + "' not found"));
        // Check if the user already has this role assigned
        if (user.getRoles().contains(role)) {
            throw new RuntimeException("User '" + username + "' already has the role '" + roleName + "'");
        }
        // If both role and user exist, assign role to the user
        user.getRoles().add(role);
        userRepository.save(user);

        log.info("Added role {} to user {}", roleName, username);
    }

    // Remove a role from a user
    @Override
    @Transactional
    public void removeRoleFromUser(String username, String roleName) {
        // If user doesn't exist display an error message
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User '" + username + "' not found."));
        // If role doesn't exist display an error message
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role '" + roleName + "' not found."));
        // If user doesn't have this role assigned display an error message
        if (!user.getRoles().contains(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User '" + username + "' does not have role '" + roleName + "'.");
        }
        // If everything is ok, remove role from the user
        user.getRoles().remove(role);
        userRepository.save(user);
        log.info("Removed role {} from user {}", roleName, username);
        }
    }
