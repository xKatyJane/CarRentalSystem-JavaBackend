package com.ironhack.carrentalsystem.service.impl;

import com.ironhack.carrentalsystem.dto.*;
import com.ironhack.carrentalsystem.mapper.UserToUserDTOMapper;
import com.ironhack.carrentalsystem.model.Booking;
import com.ironhack.carrentalsystem.model.Car;
import com.ironhack.carrentalsystem.model.Role;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.model.enums.DrivingLicenseStatus;
import com.ironhack.carrentalsystem.repository.RoleRepository;
import com.ironhack.carrentalsystem.repository.UserRepository;
import com.ironhack.carrentalsystem.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring Security method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found in the database: {}", username);
                    return new UsernameNotFoundException("User not found in the database");
                });
        log.info("User found in the database: {}", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    // Register a new user with default USER role
    public void registerUser(RegisterUserDTO registerUserDTO) {
        // Check if username is already taken
        Optional<User> existingUser = userRepository.findByUsername(registerUserDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        // If username not taken, register new user
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        // Assign default role - USER
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role 'USER' not found"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    // Save a new user, internal ADMIN function
    @Override
    @Transactional
    public void createNewUser(CreateUserDTO dto) {
        // Check if user or role already exist
        Optional<User> existingUser = userRepository.findByUsername(dto.getUsername());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        // Save the new user
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.getRoles().add(role);
        userRepository.save(newUser);
        log.info("New user '{}' created with role '{}'", dto.getUsername(), dto.getRole());
    }

    // View own info
    @Override
    @Transactional
    public User getOwnUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Update own info
    @Override
    @Transactional
    public void updateOwnInfo(String currentUsername, UpdateUserDTO dto) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean updated = false;
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
            updated = true;
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
            updated = true;
        }
        if (dto.getTelephoneNumber() != null) {
            user.setTelephoneNumber(dto.getTelephoneNumber());
            updated = true;
        }
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one field must be provided");
        }
        userRepository.save(user);
    }

    // Delete user by username
    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User '" + username + "' not found"));
        userRepository.delete(user);
        log.info("Deleted user '{}'", username);
    }

    // Get user data by username
//    @Override
    public User getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User '" + username + "' not found."));
    }

    // List all users
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserToUserDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Submit driving license for review
    @Override
    public void submitDrivingLicense(Long userId, String licenseNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setDrivingLicenseNumber(licenseNumber);
        user.setLicenseStatus(DrivingLicenseStatus.PENDING);
        userRepository.save(user);
    }

    @Override
    public List<User> getUsersByLicenseStatus(DrivingLicenseStatus status) {
        return userRepository.findByLicenseStatus(status);
    }

    @Override
    public void approveDrivingLicense(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLicenseStatus(DrivingLicenseStatus.APPROVED);
        userRepository.save(user);
    }

    @Override
    public void rejectDrivingLicense(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLicenseStatus(DrivingLicenseStatus.REJECTED);
        userRepository.save(user);
    }
}
