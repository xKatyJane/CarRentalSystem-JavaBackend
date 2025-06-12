package com.ironhack.carrentalsystem.service;

import com.ironhack.carrentalsystem.dto.CreateUserDTO;
import com.ironhack.carrentalsystem.dto.UpdateUserDTO;
import com.ironhack.carrentalsystem.dto.UserDTO;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.model.enums.DrivingLicenseStatus;

import java.util.List;

public interface UserService {

    // Update own info, each user's function
//    void updateOwnInfo(String username, UpdateUserDTO updateUserDTO);

    User getUserByUsername(String username);

    User getOwnUserEntity(String username);

    void updateOwnInfo(String currentUsername, UpdateUserDTO dto);

    // Save a new user to the database
    void createNewUser(CreateUserDTO createUserDTO);

    // Delete user
    void deleteUserByUsername(String username);

    // Retrieve a list of all users
    List<UserDTO> getAllUsers();

    // Driving license approval
    void submitDrivingLicense(Long userId, String licenseNumber);
    List<User> getUsersByLicenseStatus(DrivingLicenseStatus status);
    void approveDrivingLicense(Long userId);
    void rejectDrivingLicense(Long userId);
}