package com.example.supplytracker.DTO;



import com.example.supplytracker.Enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {



    @NotNull(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character"
    )
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;





    // Constructors
    public UserDTO() {}

    public UserDTO(String name, String email, String password) {
        //this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        //this.role = role;
    }

}