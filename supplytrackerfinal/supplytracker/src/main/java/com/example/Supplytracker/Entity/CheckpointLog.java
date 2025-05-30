package com.example.Supplytracker.Entity;

import com.example.Supplytracker.Enums.CheckpointStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity
@Table(name = "CheckpointLog") // Maps this entity to the "CheckpointLog" table
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
@Builder // Enables the builder pattern for this class
public class CheckpointLog {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private long id;

    @OneToOne // Defines a one-to-one relationship with the Shipment entity
    @JoinColumn(name = "shipment_id") // Maps the foreign key column
    private Shipment shipment;

    // The location where the checkpoint was logged
    private String location;

    // Enum representing the status of the checkpoint (e.g., ARRIVED, DEPARTED)
    @Enumerated(EnumType.STRING) // Stores the enum as a string in the database
    private CheckpointStatus status;

    // Timestamp when the checkpoint was created
    private LocalDateTime timestamp;
}
