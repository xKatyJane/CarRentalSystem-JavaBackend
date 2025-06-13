package com.ironhack.carrentalsystem.controller;

import com.ironhack.carrentalsystem.dto.*;
import com.ironhack.carrentalsystem.model.User;
import com.ironhack.carrentalsystem.repository.UserRepository;
import com.ironhack.carrentalsystem.service.UserService;
import com.ironhack.carrentalsystem.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    // List all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Register as a new user with role USER
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
        return ResponseEntity.ok(String.format("User %s registered successfully as USER", registerUserDTO.getUsername()));
    }

    // Create a new user from the ADMIN side
    @PostMapping("/newUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createNewUser(@RequestBody CreateUserDTO createUserDTO) {
        userService.createNewUser(createUserDTO);
        return ResponseEntity.ok(String.format("New user %s created successfully.", createUserDTO.getUsername()));
    }

    // View own data
    @GetMapping("/me")
    public ResponseEntity<User> getOwnUserInfo(Principal principal) {
        User user = userService.getOwnUserEntity(principal.getName());
        // Remove sensitive data before returning
        user.setPassword(null);
        user.setRoles(null);
        return ResponseEntity.ok(user);
    }

    // Edit own data
    @PatchMapping("/me")
    public ResponseEntity<String> editOwnProfile(@RequestBody UpdateUserDTO dto, Principal principal) {
        userService.updateOwnInfo(principal.getName(), dto);
        return ResponseEntity.ok("User profile updated successfully.");
    }

    // Delete user
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        // Get the current authenticated user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername;
        if (principal instanceof UserDetails) {
            loggedInUsername = ((UserDetails) principal).getUsername();
        } else {
            loggedInUsername = principal.toString();
        }
        // Prevent user from deleting themselves
        if (username.equalsIgnoreCase(loggedInUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete yourself.");
        }
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("User '" + username + "' deleted successfully.");
    }
}