package com.example.supplytrackersystem.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryPerformanceDto {

    @NotNull(message = " ")
    private long totalShipments;

    @NotNull(message = "")
    private long onTimeDeliveries;

    @NotNull(message = "")
    private long delayedDeliveries;

    public DeliveryPerformanceDto(long totalShipments, long onTimeDeliveries, long delayedDeliveries){
        this.totalShipments = totalShipments;
        this.onTimeDeliveries = onTimeDeliveries;
        this.delayedDeliveries = delayedDeliveries;
    }
}
