package com.example.Supplytracker.Repository;

import com.example.Supplytracker.Entity.CheckpointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CheckpointLogRepository extends JpaRepository<CheckpointLog, Long> {

    List<CheckpointLog> findByShipmentId(Long shipmentId);
}
