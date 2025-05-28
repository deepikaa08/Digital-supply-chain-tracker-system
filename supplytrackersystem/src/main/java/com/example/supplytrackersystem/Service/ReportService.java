package com.example.supplytrackersystem.Service;

import com.example.supplytracker.DTO.DelayedShipmentDto;
import com.example.supplytracker.DTO.DeliveryPerformanceDto;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Repository.CheckpointLogRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReportService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private CheckpointLogRepository checkpointLogRepository;;

    public DeliveryPerformanceDto getDeliveryPerformance() {
        List<Shipment> all = shipmentRepository.findAll();
        long total = all.size();
        long onTime = 0;
        long delayed = 0;

        for (Shipment shipment : all) {
            // Get all checkpoint logs for this shipment
            List<CheckpointLog> logs = checkpointLogRepository.findByShipmentId(shipment.getId());

            // Find the 'Delivered' status log
            CheckpointLog deliveredLog = null;
            for (CheckpointLog log : logs) {
                if ("Delivered".equalsIgnoreCase(log.getStatus().name())) {
                    deliveredLog = log;
                    break;
                }
            }

            if (deliveredLog!= null) {
                LocalDateTime deliveredAt = deliveredLog.getTimestamp();
                LocalDate expectedDate = shipment.getExpectedDelivery();

                if (!deliveredAt.toLocalDate().isAfter(expectedDate)) {
                    onTime++;
                } else {
                    delayed++;
                }
            }
        }

        return new DeliveryPerformanceDto(total, onTime, delayed);
    }

    public List<DelayedShipmentDto> getDelayedShipments() {
        List<Shipment> allShipments = shipmentRepository.findAll();
        List<DelayedShipmentDto> delayedShipments = new ArrayList<>();

        for (Shipment shipment : allShipments) {
            List<CheckpointLog> logs = checkpointLogRepository.findByShipmentId(shipment.getId());

            CheckpointLog deliveredLog = null;
            for (CheckpointLog log : logs) {
                if ("DELIVERED".equalsIgnoreCase(log.getStatus().name())) {
                    deliveredLog = log;
                    break;
                }
            }

            if (deliveredLog != null) {
                LocalDate deliveredDate = deliveredLog.getTimestamp().toLocalDate();
                LocalDate expectedDate = shipment.getExpectedDelivery();

                if (deliveredDate.isAfter(expectedDate)) {
                    long delayDays = ChronoUnit.DAYS.between(expectedDate, deliveredDate);

                    DelayedShipmentDto dto = new DelayedShipmentDto(
                            shipment.getId(),
                            shipment.getItemId(),
                            expectedDate,
                            deliveredLog.getTimestamp(),
                            delayDays
                    );

                    delayedShipments.add(dto);
                }
            }
        }

        return delayedShipments;
    }

}
