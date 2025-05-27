package com.example.supplytracker.DTO;

import jakarta.validation.constraints.NotNull;
import com.example.supplytracker.Enums.CheckpointStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckpointLogDTO {

    @NotNull(message = "Shipment Id is required!! ")
    private Long shipmentId;

    @NotNull(message = "Location is required!!")
    private String location;

    @NotNull(message = "Status cannot be empty!!")
    private CheckpointStatus status;
}
