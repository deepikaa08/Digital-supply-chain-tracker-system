package com.example.supplytracker.ControllerTest;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.Controller.CheckpointLogController;
import com.example.supplytracker.DTO.CheckpointLogDTO;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.CheckpointStatus;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Service.CheckpointLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckpointLogControllerTest {

    private CheckpointLogController checkpointLogController;
    private CheckpointLogService checkpointLogService;
    private Shipment shipment;

    private CheckpointLogDTO checkpointLogDTO;
    private CheckpointLog checkpointLog;
    private User transporterUser;

    @BeforeEach
    void setUp() {
        shipment = new Shipment(12L,"abc","def", LocalDate.of(2025, 6, 15));
        checkpointLogService = mock(CheckpointLogService.class);
        checkpointLogController = new CheckpointLogController(checkpointLogService);

        checkpointLogDTO = new CheckpointLogDTO(shipment ,"Warehouse A", CheckpointStatus.RECIEVED);
        checkpointLog = new CheckpointLog(1L,shipment,  "Warehouse A", CheckpointStatus.RECIEVED, LocalDateTime.now());

        transporterUser = new User();
        transporterUser.setId(1L);
        transporterUser.setName("transporter1");
        transporterUser.setRole(Role.TRANSPORTER);
    }

    @Test
    void testAddCheckpoint_Unauthenticated() {
        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class)) {
            mockedSession.when(SessionManager::isLoggedIn).thenReturn(false);

            ResponseEntity<?> response = checkpointLogController.addCheckpoint(checkpointLogDTO);
            assertEquals(401, response.getStatusCodeValue());
            assertEquals("Please log in first.", response.getBody());
        }
    }

    @Test
    void testAddCheckpoint_ForbiddenForNonTransporter() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);

        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class)) {
            mockedSession.when(SessionManager::isLoggedIn).thenReturn(true);
            mockedSession.when(SessionManager::getCurrentUser).thenReturn(adminUser);

            ResponseEntity<?> response = checkpointLogController.addCheckpoint(checkpointLogDTO);
            assertEquals(403, response.getStatusCodeValue());
            assertEquals("Only TRANSPORTERs can add checkpoints.", response.getBody());
        }
    }

    @Test
    void testAddCheckpoint_Success() {
        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class)) {
            mockedSession.when(SessionManager::isLoggedIn).thenReturn(true);
            mockedSession.when(SessionManager::getCurrentUser).thenReturn(transporterUser);

            when(checkpointLogService.addCheckpoint(checkpointLogDTO)).thenReturn(checkpointLog);

            ResponseEntity<?> response = checkpointLogController.addCheckpoint(checkpointLogDTO);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(checkpointLog, response.getBody());

            verify(checkpointLogService).addCheckpoint(checkpointLogDTO);
        }
    }

    @Test
    void testGetByShipment_Unauthenticated() {
        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class)) {
            mockedSession.when(SessionManager::isLoggedIn).thenReturn(false);

            ResponseEntity<?> response = checkpointLogController.getByShipment(shipment);
            assertEquals(401, response.getStatusCodeValue());
            assertEquals("Please log in first.", response.getBody());
        }
    }

    @Test
    void testGetByShipment_Success() {
        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class)) {
            mockedSession.when(SessionManager::isLoggedIn).thenReturn(true);

            when(checkpointLogService.getByShipmentId(shipment)).thenReturn(List.of(checkpointLog));

            ResponseEntity<?> response = checkpointLogController.getByShipment(shipment);
            assertEquals(200, response.getStatusCodeValue());
            List<CheckpointLog> logs = (List<CheckpointLog>) response.getBody();

            assertNotNull(logs);
            assertEquals(1, logs.size());
            assertEquals("Warehouse A", logs.get(0).getLocation());
        }
    }
}
