package com.example.supplytracker.DTO;

import com.example.supplytracker.Enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {

    // Name must be present and between 2 to 50 characters
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    // Email must be present and in a valid format
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Password must meet security criteria: uppercase, lowercase, digit, special character
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character"
    )
    private String password;

    // Default constructor
    public UserDTO() {}

    // Parameterized constructor
    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
