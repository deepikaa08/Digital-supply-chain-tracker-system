package com.example.supplytracker.Entity;

import com.example.supplytracker.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")  // Maps this entity to the "users" table in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremented primary key
    private long id;

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)  // Store enum as a string (e.g., "ADMIN", "USER")
    private Role role;
}
