package com.example.Supplytracker.ControllerTest;

import com.example.Supplytracker.Config.SessionManager;
import com.example.Supplytracker.Controller.CheckpointLogController;
import com.example.Supplytracker.DTO.CheckpointLogDTO;
import com.example.Supplytracker.Entity.CheckpointLog;
import com.example.Supplytracker.Entity.Shipment;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.CheckpointStatus;
import com.example.Supplytracker.Enums.Role;
import com.example.Supplytracker.Service.CheckpointLogService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckpointLogControllerTest {

    private CheckpointLogController controller;
    private CheckpointLogService service;

    private Shipment shipment;
    private CheckpointLogDTO dto;
    private CheckpointLog checkpoint;
    private User transporter;

    @BeforeEach
    void setup() {
        shipment = new Shipment(12L, "abc", "def", LocalDate.of(2025, 6, 15));
        service = mock(CheckpointLogService.class);
        controller = new CheckpointLogController(service);

        dto = new CheckpointLogDTO(shipment, "Warehouse A", CheckpointStatus.RECIEVED);
        checkpoint = new CheckpointLog(1L, shipment, "Warehouse A", CheckpointStatus.RECIEVED, LocalDateTime.now());

        transporter = new User();
        transporter.setRole(Role.TRANSPORTER);

        // Make sure no user is logged in before each test (clean static state)
        SessionManager.logout();
    }

    @AfterEach
    void cleanup() {
        // Clear static state after each test to avoid pollution
        SessionManager.logout();
    }

    @Test
    void testAddCheckpoint_Unauthenticated() {
        // No login done here, so SessionManager.isLoggedIn() should be false

        var response = controller.addCheckpoint(dto);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Please log in first.", response.getBody());
    }

    @Test
    void testAddCheckpoint_ForbiddenForNonTransporter() {
        User admin = new User();
        admin.setRole(Role.ADMIN);
        SessionManager.login(admin);  // Log in admin user

        var response = controller.addCheckpoint(dto);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only TRANSPORTERs can add checkpoints.", response.getBody());
    }

    @Test
    void testAddCheckpoint_Success() {
        SessionManager.login(transporter); // Log in transporter user

        when(service.addCheckpoint(dto)).thenReturn(checkpoint);

        var response = controller.addCheckpoint(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(checkpoint, response.getBody());
        verify(service).addCheckpoint(dto);
    }



}
