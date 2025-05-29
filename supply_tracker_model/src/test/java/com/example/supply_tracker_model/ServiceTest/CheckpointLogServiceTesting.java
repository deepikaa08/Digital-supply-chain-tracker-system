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

import static com.example.supplytracker.Enums.CheckpointStatus.RECIEVED;
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

    @BeforeEach
    void setup() {
        shipment = new Shipment();
        shipment.setId(1L);

        lenient().when(shipmentRepository.findById(anyLong())).thenReturn(Optional.of(shipment));

        checkpointLog = new CheckpointLog();
        checkpointLog.setId(1L);
        checkpointLog.setShipment(shipment);
        checkpointLog.setLocation("abc");
        checkpointLog.setStatus(RECIEVED);
        checkpointLog.setTimestamp(LocalDateTime.now());

        checkpointLogDTO = new CheckpointLogDTO(shipment, "abc", RECIEVED);
    }

    @Test
    void testAddCheckpoint() {
        when(checkpointLogRepository.save(any(CheckpointLog.class))).thenReturn(checkpointLog);

        CheckpointLog addedCheckpoint = checkpointLogServiceImplementation.addCheckpoint(checkpointLogDTO);

        assertNotNull(addedCheckpoint);
        assertEquals("abc", addedCheckpoint.getLocation());
    }

    @Test
    void testGetByShipmentId() {
        when(checkpointLogRepository.findByShipmentId(anyLong())).thenReturn(List.of(checkpointLog));

        List<CheckpointLog> result = checkpointLogServiceImplementation.getByShipmentId(shipment);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("abc", result.get(0).getLocation());
    }
}
