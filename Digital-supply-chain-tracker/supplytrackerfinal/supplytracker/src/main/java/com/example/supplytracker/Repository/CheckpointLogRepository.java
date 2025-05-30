package com.example.supplytracker.Repository;

import com.example.supplytracker.Entity.CheckpointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CheckpointLogRepository extends JpaRepository<CheckpointLog, Long> {


    @Query("SELECT c FROM CheckpointLog c WHERE c.shipment.shipmentId = :shipmentId")
    List<CheckpointLog> findByShipmentId(@Param("shipmentId") Long shipmentId);



}
