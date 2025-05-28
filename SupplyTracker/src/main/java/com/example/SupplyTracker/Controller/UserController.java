package com.example.supplytracker.Controller;



import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.DTO.UserDTO;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.EmailAlreadyExistsException;
import com.example.supplytracker.Exceptions.UserNotFoundException;
import com.example.supplytracker.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name= "User")
public class UserController {

    private final UserService userService;

    @Autowired  // Optional since Spring 4.3 if there's only one constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) throws EmailAlreadyExistsException {
        User savedUser = userService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully with ID: " + savedUser.getId());
    }
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .map(user -> {
                    SessionManager.login(user);  // Store logged-in user
                    return ResponseEntity.ok("Login successful for user: " + user.getName());
                })
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(400).body("No user is logged in");
        }
        SessionManager.logout();
        return ResponseEntity.ok("Logged out successfully");
    }


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