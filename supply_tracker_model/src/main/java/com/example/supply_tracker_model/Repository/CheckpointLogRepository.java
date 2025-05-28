package com.example.supplytracker.Repository;

import com.example.supplytracker.Entity.CheckpointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckpointLogRepository extends JpaRepository<CheckpointLog, Long> {
    List<CheckpointLog> findByShipmentId(Long shipmentId);
}
