package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.DelayedShipmentDto;
import com.example.supplytracker.DTO.DeliveryPerformanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Repository.CheckpointLogRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;


@Service  // Marks this class as a service layer in Spring
public class ReportService {

    @Autowired
    private ShipmentRepository shipmentRepository;   // To fetch shipment data

    @Autowired
    private CheckpointLogRepository checkpointLogRepository;  // To fetch delivery logs
    // Method to get overall delivery performance report
    public DeliveryPerformanceDto getDeliveryPerformance() {
        List<Shipment> all = shipmentRepository.findAll();  // Get all shipments
        long total = all.size();  // Total number of shipments
        long onTime = 0;  // Counter for on-time deliveries
        long delayed = 0;  // Counter for delayed deliveries

        for (Shipment shipment : all) {
            // Get logs for each shipment
            List<CheckpointLog> logs = checkpointLogRepository.findByShipmentId(shipment.getShipmentId());

            // Find the log where shipment was delivered
            CheckpointLog deliveredLog = null;
            for (CheckpointLog log : logs) {
                if ("Delivered".equalsIgnoreCase(log.getStatus().name())) {
                    deliveredLog = log;
                    break;
                }
            }

            if (deliveredLog!= null) {
                LocalDateTime deliveredAt = deliveredLog.getTimestamp();  // Date/time it was delivered
                LocalDate expectedDate = shipment.getExpectedDelivery();  // Expected delivery date
                // If delivered on or before expected date
                if (!deliveredAt.toLocalDate().isAfter(expectedDate)) {
                    onTime++;   // Count as on-time
                } else {
                    delayed++;  // Count as delayed
                }
            }
        }
        // Return the final result with totals
        return new DeliveryPerformanceDto(total, onTime, delayed);
    }
    // Method to get a list of delayed shipments with details
    public List<DelayedShipmentDto> getDelayedShipments() {
        List<Shipment> allShipments = shipmentRepository.findAll();  // Get all shipments
        List<DelayedShipmentDto> delayedShipments = new ArrayList<>();  // Store delayed ones

        for (Shipment shipment : allShipments) {
            // Get logs for this shipment
            List<CheckpointLog> logs = checkpointLogRepository.findByShipmentId(shipment.getShipmentId());
            // Find the delivered log
            CheckpointLog deliveredLog = null;
            for (CheckpointLog log : logs) {
                if ("DELIVERED".equalsIgnoreCase(log.getStatus().name())) {
                    deliveredLog = log;
                    break;
                }
            }
            // Check if delivery was late
            if (deliveredLog != null) {
                LocalDate deliveredDate = deliveredLog.getTimestamp().toLocalDate();
                LocalDate expectedDate = shipment.getExpectedDelivery();

                if (deliveredDate.isAfter(expectedDate)) {
                    // Calculate number of delayed days
                    long delayDays = ChronoUnit.DAYS.between(expectedDate, deliveredDate);
                    // Create DTO with delay info
                    DelayedShipmentDto dto = new DelayedShipmentDto(
                            shipment.getShipmentId(),
                            shipment.getItemId(),
                            expectedDate,
                            deliveredLog.getTimestamp(),
                            delayDays
                    );
                    // Add to result list
                    delayedShipments.add(dto);
                }
            }
        }

        return delayedShipments;   // Return list of delayed shipments
    }

}
