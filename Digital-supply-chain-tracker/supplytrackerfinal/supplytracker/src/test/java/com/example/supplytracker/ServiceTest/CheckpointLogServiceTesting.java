package com.example.supplytracker.ServiceTest;

import com.example.supplytracker.DTO.CheckpointLogDTO;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Repository.CheckpointLogRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import com.example.supplytracker.Service.CheckpointLogServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.supplytracker.Enums.CheckpointStatus.RECEIVED;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckpointLogServiceTesting {

    @Mock
    CheckpointLogRepository checkpointLogRepository;

    @Mock
    ShipmentRepository shipmentRepository;

    @InjectMocks
    CheckpointLogServiceImplementation checkpointLogServiceImplementation;

    private CheckpointLogDTO checkpointLogDTO;
    private CheckpointLog checkpointLog;
    private Shipment shipment;

   /* @BeforeEach
    void setup() {
        shipment = new Shipment();
        shipment.setShipmentId(12L);

        // Mocking shipment lookup
        lenient().when(shipmentRepository.findById(anyLong())).thenReturn(Optional.of(shipment));

        // Setting up dummy checkpoint log
        checkpointLog = new CheckpointLog();
        checkpointLog.setId(1L);
        checkpointLog.setShipment(shipment);
        checkpointLog.setLocation("abc");
        checkpointLog.setStatus(RECIEVED);
        checkpointLog.setTimestamp(LocalDateTime.now());

        // Creating a DTO to simulate input
        checkpointLogDTO = new CheckpointLogDTO(12L, "abc", RECIEVED);
    }*/

    @BeforeEach
    void setup() {
        shipment = new Shipment();
        shipment.setShipmentId(12L);  // Match the DTO shipmentId

        checkpointLogDTO = new CheckpointLogDTO(12L, "abc", RECEIVED);

        checkpointLog = new CheckpointLog();
        checkpointLog.setId(1L);
        checkpointLog.setShipment(shipment);
        checkpointLog.setLocation("abc");
        checkpointLog.setStatus(RECEIVED);
        checkpointLog.setTimestamp(LocalDateTime.now());
    }

/*
    @Test
    void testAddCheckpoint() {
        // Mock saving checkpoint
        when(checkpointLogRepository.save(any(CheckpointLog.class))).thenReturn(checkpointLog);

        CheckpointLog addedCheckpoint = checkpointLogServiceImplementation.addCheckpoint(checkpointLogDTO);

        // Verify result
        assertNotNull(addedCheckpoint);
        assertEquals("abc", addedCheckpoint.getLocation());
    }
*/
    @Test
    void testAddCheckpoint() {
        // Arrange: Ensure the shipment ID in DTO and the mock return match
        shipment.setShipmentId(checkpointLogDTO.getShipmentId());
        when(shipmentRepository.findById(checkpointLogDTO.getShipmentId()))
                .thenReturn(Optional.of(shipment));

        // Mock the save operation
        when(checkpointLogRepository.save(any(CheckpointLog.class)))
                .thenReturn(checkpointLog);

        // Act
        CheckpointLog addedCheckpoint = checkpointLogServiceImplementation.addCheckpoint(checkpointLogDTO);

        // Assert
        assertNotNull(addedCheckpoint);
        assertEquals("abc", addedCheckpoint.getLocation());
    }



    @Test
    void testGetByShipmentId() {
        // Arrange
        when(checkpointLogRepository.findByShipmentId(anyLong())).thenReturn(List.of(checkpointLog));

        // Act
        List<CheckpointLog> result = checkpointLogServiceImplementation.getByShipmentId(shipment.getShipmentId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("abc", result.get(0).getLocation());
    }
}
