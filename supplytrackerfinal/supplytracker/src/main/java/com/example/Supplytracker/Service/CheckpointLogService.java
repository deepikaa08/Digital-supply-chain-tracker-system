package com.example.Supplytracker.Service;

import com.example.Supplytracker.DTO.CheckpointLogDTO;
import com.example.Supplytracker.Entity.CheckpointLog;
import com.example.Supplytracker.Entity.Shipment;

import java.util.List;

// Service interface for handling checkpoint log operations
public interface CheckpointLogService {

    // Adds a new checkpoint based on the provided DTO
    CheckpointLog addCheckpoint(CheckpointLogDTO dto);

    // Retrieves a list of checkpoint logs associated with a specific shipment
    List<CheckpointLog> getByShipmentId(Shipment shipment);
}
