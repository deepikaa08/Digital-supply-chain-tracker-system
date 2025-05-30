package com.example.Supplytracker.Repository;

import com.example.Supplytracker.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
}
