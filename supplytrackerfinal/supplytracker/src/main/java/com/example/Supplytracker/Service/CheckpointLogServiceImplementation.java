package com.example.Supplytracker.Service;

import com.example.Supplytracker.DTO.CheckpointLogDTO;
import com.example.Supplytracker.Entity.CheckpointLog;
import com.example.Supplytracker.Entity.Shipment;
import com.example.Supplytracker.Repository.CheckpointLogRepository;
import com.example.Supplytracker.Repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates a constructor for final fields
public class CheckpointLogServiceImplementation implements CheckpointLogService {

    @Autowired
    ShipmentRepository shipmentrepo; // Unused field – can be removed

    // Repository for accessing checkpoint log data
    private final CheckpointLogRepository checkpointLogRepository;

    // Repository for accessing shipment data
    private final ShipmentRepository shipmentRepository;

    // Adds a new checkpoint log based on the provided DTO
    @Override
    public CheckpointLog addCheckpoint(CheckpointLogDTO dto) {
        // Retrieve shipment by ID, throw error if not found
        Shipment shipment = shipmentRepository.findById(dto.getShipment().getId())
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        // Build a new CheckpointLog entity
        CheckpointLog log = CheckpointLog.builder()
                .shipment(shipment)
                .location(dto.getLocation())
                .status(dto.getStatus())
                .timestamp(LocalDateTime.now()) // Set current timestamp
                .build();

        // Save and return the new checkpoint log
        return checkpointLogRepository.save(log);
    }

    // Retrieves all checkpoint logs for a given shipment
    @Override
    public List<CheckpointLog> getByShipmentId(Shipment shipment) {
        return checkpointLogRepository.findByShipmentId(shipment.getId());
    }
}
