package com.example.supplytrackersystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DelayedShipmentDto {
    private Long shipmentId;
    private Long supplierId;
    private LocalDate expectedDeliveryDate;
    private LocalDateTime deliveredAt;
    private long delayedByDays;
}
