package com.example.supplytracker.ServiceTest;


import com.example.supplytracker.DTO.DelayedShipmentDto;
import com.example.supplytracker.DTO.DeliveryPerformanceDto;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Enums.CheckpointStatus;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Repository.CheckpointLogRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import com.example.supplytracker.Service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private CheckpointLogRepository checkpointLogRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeliveryPerformance() {
        Shipment shipment1 = new Shipment();
        shipment1.setShipmentId(1L);
        shipment1.setItemId(1001L);
        shipment1.setExpectedDelivery(LocalDate.of(2024, 5, 25));

        Shipment shipment2 = new Shipment();
        shipment2.setShipmentId(2L);
        shipment2.setItemId(1002L);
        shipment2.setExpectedDelivery(LocalDate.of(2024, 5, 25));

        CheckpointLog log1 = CheckpointLog.builder()
                .id(1L)
                .status(CheckpointStatus.DELIVERED)
                .timestamp(LocalDateTime.of(2024, 5, 24, 10, 0))
                .build();

        CheckpointLog log2 = CheckpointLog.builder()
                .id(2L)
                .status(CheckpointStatus.DELIVERED)
                .timestamp(LocalDateTime.of(2024, 5, 26, 10, 0))
                .build();

        when(shipmentRepository.findAll()).thenReturn(List.of(shipment1, shipment2));
        when(checkpointLogRepository.findByShipmentId(1L)).thenReturn(List.of(log1));
        when(checkpointLogRepository.findByShipmentId(2L)).thenReturn(List.of(log2));

        DeliveryPerformanceDto result = reportService.getDeliveryPerformance();

        assertEquals(2, result.getTotalShipments());
        assertEquals(1, result.getOnTimeDeliveries());
        assertEquals(1, result.getDelayedDeliveries());
    }

    @Test
    void testGetDelayedShipments() {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1L);
        shipment.setItemId(2001L);
        shipment.setExpectedDelivery(LocalDate.of(2024, 5, 25));

        CheckpointLog deliveredLog = CheckpointLog.builder()
                .id(1L)
                .status(CheckpointStatus.DELIVERED)
                .timestamp(LocalDateTime.of(2024, 5, 27, 12, 0))
                .build();


        when(shipmentRepository.findAll()).thenReturn(List.of(shipment));
        when(checkpointLogRepository.findByShipmentId(1L)).thenReturn(List.of(deliveredLog));

        List<DelayedShipmentDto> result = reportService.getDelayedShipments();

        assertEquals(1, result.size());
        DelayedShipmentDto dto = result.get(0);
        assertEquals(1L, dto.getShipmentId());
        assertEquals(2, dto.getDelayDays());
        assertEquals(LocalDate.of(2024, 5, 25), dto.getExpectedDate());
        assertEquals(LocalDateTime.of(2024, 5, 27, 12, 0), dto.getDeliveredDate());
    }
}