package com.example.supplytracker.Entity;


import com.example.supplytracker.Enums.CheckpointStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table (name = "Checkpoint Log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckpointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

   private long id;

    @OneToOne
    private  long shipmentId;

    private String location;

    @Enumerated(EnumType.STRING)
    private CheckpointStatus status;

    private LocalDateTime timestamp;



}
