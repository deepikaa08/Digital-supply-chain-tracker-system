package com.example.Supplytracker.Controller;

import com.example.Supplytracker.Config.SessionManager;
import com.example.Supplytracker.DTO.UserDTO;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.Role;
import com.example.Supplytracker.Exceptions.EmailAlreadyExistsException;
import com.example.Supplytracker.Exceptions.UserNotFoundException;
import com.example.Supplytracker.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name= "User")
public class UserController {

    private final UserService userService;

    @Autowired  // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user registration
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) throws EmailAlreadyExistsException {
        User savedUser = userService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully with ID: " + savedUser.getId());
    }

    // Endpoint for user login
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .map(user -> {
                    SessionManager.login(user);  // Store logged-in user
                    return ResponseEntity.ok("Login successful for user: " + user.getName());
                })
                .orElse(ResponseEntity.status(401).body("Invalid credentials")); // If login fails
    }

    // Endpoint for user logout
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(400).body("No user is logged in");
        }
        SessionManager.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    // Get all users (Admin only)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please login first");
        }

        User currentUser = SessionManager.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Update user role (Admin only)
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam Role role) throws UserNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please login first");
        }

        User currentUser = SessionManager.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Only admins can update roles");
        }

        userService.updateUserRole(id, role);
        return ResponseEntity.ok("User role updated");
    }

}
