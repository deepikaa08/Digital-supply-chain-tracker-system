package com.example.Supplytracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data  // Automatically creates getters, setters, toString(), equals(), and hashCode()
@AllArgsConstructor   // Creates a constructor with all the fields as arguments
public class DelayedShipmentDto {

    // ID of the shipment
    private Long shipmentId;
    // ID of the supplier who sent the shipment
    private Long supplierId;
    // The expected delivery date for the shipment
    private LocalDate expectedDeliveryDate;
    // The actual date and time the shipment was delivered
    private LocalDateTime deliveredAt;
    // Number of days the delivery was late
    private long delayedByDays;
    // Getter for delayed days
    public long getDelayDays() {
        return delayedByDays;
    }
    // Getter for expected delivery date
    public LocalDate getExpectedDate()
    {
        return expectedDeliveryDate;
    }
    // Getter for actual delivered date
    public LocalDateTime getDeliveredDate() {
        return deliveredAt;
    }
}
