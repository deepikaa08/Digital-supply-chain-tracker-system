package com.example.supplytracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ShipmentRequestDTO {
    private long itemId;
    @NotNull(message = "From Location is required")
    private String fromLocation;
    @NotNull(message = "To Location is required")
    private String toLocation;
    private LocalDate expectedDelivery;
}
