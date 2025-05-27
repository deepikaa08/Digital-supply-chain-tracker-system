package com.example.supplytracker.Entity;

import com.example.supplytracker.Enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Data
@Table(name = "Shipment")

public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long itemId;
    private String fromLocation;
    private String toLocation;
    private LocalDate expectedDelivery;

    private long transporterId;

    @Enumerated(EnumType.STRING)
    private Status currentStatus = Status.CREATED;

    public Shipment(long itemId, String fromLocation, String toLocation, LocalDate expectedDelivery) {
        this.itemId = itemId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;

        this.expectedDelivery = expectedDelivery;
    }

    public Shipment() {
    }

}
