package com.example.supplytracker.DTO;

import com.example.supplytracker.Entity.Shipment;
import jakarta.validation.constraints.NotNull;
import com.example.supplytracker.Enums.CheckpointStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
public class CheckpointLogDTO {

    // Shipment associated with the checkpoint

    private Shipment shipment;
    @NotNull
    private Long shipmentId;


    // Location of the checkpoint (required)
    @NotNull(message = "Location is required!!")
    private String location;

    // Status of the checkpoint (required)
    @NotNull(message = "Status cannot be empty!!")
    private CheckpointStatus status;



    public CheckpointLogDTO(long l, String abc, CheckpointStatus checkpointStatus) {
        this.shipmentId = l;
        this.location = abc;
        this.status = checkpointStatus;
    }
}
