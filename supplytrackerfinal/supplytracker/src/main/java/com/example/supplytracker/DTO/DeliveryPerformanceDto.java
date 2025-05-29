package com.example.supplytracker.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter  // Automatically creates getter methods for all fields
@Setter  // Automatically creates setter methods for all fields
public class DeliveryPerformanceDto {

    @NotNull(message = "Cannot be empty")// This field cannot be null (validation)
    private long totalShipments;  // Total number of shipments

    @NotNull(message = "Cannot be empty")  // Cannot be null
    private long onTimeDeliveries;  // Number of shipments delivered on time

    @NotNull(message = "Cannot be empty")  // Cannot be null
    private long delayedDeliveries;  // Number of shipments that were delayed
    // Constructor to set all three values when creating the object
    public DeliveryPerformanceDto(long totalShipments, long onTimeDeliveries, long delayedDeliveries){
        this.totalShipments = totalShipments;
        this.onTimeDeliveries = onTimeDeliveries;
        this.delayedDeliveries = delayedDeliveries;
    }
}
