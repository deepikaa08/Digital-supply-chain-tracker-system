package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.CheckpointLogDTO;
import com.example.supplytracker.Entity.CheckpointLog;

import java.util.List;

public interface CheckpointLogService {
    CheckpointLog addCheckpoint(CheckpointLogDTO dto);
        List<CheckpointLog> getByShipmentId(Long shipmentId);

}
