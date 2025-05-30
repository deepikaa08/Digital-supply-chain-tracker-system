package com.example.supplytracker.ControllerTest;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.Controller.ShipmentController;
import com.example.supplytracker.DTO.ShipmentRequestDTO;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.ShipmentNotFoundException;
import com.example.supplytracker.Exceptions.TransporterNotFoundException;
import com.example.supplytracker.Service.ShipmentServiceLayer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class ShipmentControllerTest {

    User mockUser = new User(); // Mock user to simulate logged-in user
    private ShipmentServiceLayer service; // Mocked service layer
    private ShipmentController controller; // Controller under test

    @BeforeEach
    public void setup() {
        service = Mockito.mock(ShipmentServiceLayer.class); // Mock the service
        controller = new ShipmentController();
        controller.service = service; // Inject mock service into controller

        // Setup a mock logged-in admin user
        mockUser.setId(1L);
        mockUser.setName("Test");
        mockUser.setEmail("Test@junit.com");
        mockUser.setPassword("TestPassword");
        mockUser.setRole(Role.ADMIN);

        SessionManager.login(mockUser); // Simulate user login
    }

    @Test
    public void getTest() {
        // Arrange: prepare mock data
        List<Shipment> mockShipments = Arrays.asList(new Shipment(), new Shipment());
        when(service.getAll()).thenReturn(mockShipments);

        // Act: call controller method
        ResponseEntity<?> response = controller.get();

        // Assert: validate response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockShipments, response.getBody());
    }

    @Test
    public void getByIdTest() throws ShipmentNotFoundException {
        // Arrange: mock return of a shipment by ID
        Shipment mockShipment = new Shipment();
        mockShipment.setShipmentId(1L);
        when(service.getWithId(1L)).thenReturn(mockShipment);

        // Act
        ResponseEntity<?> response = controller.getById(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockShipment, response.getBody());
    }

    @Test
    public void addShipmentTest() throws ItemNotFoundException {
        // Arrange: simulate supplier user and mock add logic
        mockUser.setRole(Role.SUPPLIER);
        ShipmentRequestDTO dto = new ShipmentRequestDTO();
        dto.setItemId(11L);

        Shipment mockShipment = new Shipment();
        mockShipment.setShipmentId(11L);
        when(service.add(dto)).thenReturn(mockShipment);

        // Act
        ResponseEntity<?> response = controller.addShipment(dto);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockShipment, response.getBody());
    }

    @Test
    public void updateStatusTest() throws ShipmentNotFoundException {
        // Arrange: simulate transporter and update status
        mockUser.setRole(Role.TRANSPORTER);
        Shipment mockShipment = new Shipment();
        when(service.updateStatus(1L, Status.IN_TRANSIT)).thenReturn(mockShipment);

        // Act
        ResponseEntity<?> response = controller.updateStatus(1L, Status.IN_TRANSIT);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Status updated for Shipment with id 1 as IN_TRANSIT", response.getBody());
    }

    @Test
    public void assignTransporterTest() throws ShipmentNotFoundException, TransporterNotFoundException {
        // Arrange: mock assigning transporter
        Shipment mockShipment = new Shipment();
        when(service.assign(1L, 10L)).thenReturn(mockShipment);

        // Act
        ResponseEntity<?> response = controller.assignTransporter(1L, 10L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transporter updated for Shipment id 1 with transporter id 10", response.getBody());
    }
}
