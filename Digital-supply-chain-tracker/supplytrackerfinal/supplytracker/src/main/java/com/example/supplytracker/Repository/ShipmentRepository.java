package com.example.supplytracker.Repository;

import com.example.supplytracker.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
}
