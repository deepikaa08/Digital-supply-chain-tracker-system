package com.example.supplytracker.Enums;

// Enum representing the possible statuses of a shipment at a checkpoint
public enum CheckpointStatus {

    // Shipment has been received at the checkpoint
    RECEIVED ,

    // Shipment is currently in transit
    IN_TRANSIT,

    // Shipment has been damaged
    DAMAGED,

    // Shipment has been successfully delivered
    DELIVERED
}
