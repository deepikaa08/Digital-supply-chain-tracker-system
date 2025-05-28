package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.CheckpointLogDTO;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Repository.CheckpointLogRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CheckpointLogServiceImplementation implements CheckpointLogService {

    private CheckpointLogRepository checkpointLogRepository;
    private ShipmentRepository shipmentRepository;

    @Override
    public CheckpointLog addCheckpoint(CheckpointLogDTO dto) {


        CheckpointLog log = CheckpointLog.builder()
                .shipmentId(dto.getShipmentId())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .timestamp(LocalDateTime.now())
                .build();

        return checkpointLogRepository.save(log);
    }

    @Override
    public List<CheckpointLog> getByShipmentId(Long shipmentId) {
        return checkpointLogRepository.findByShipmentId(shipmentId);
    }
}
