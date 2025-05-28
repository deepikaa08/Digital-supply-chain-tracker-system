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

    User mockUser = new User();
    private ShipmentServiceLayer service;
    private ShipmentController controller;
    @BeforeEach
    public void setup(){
        service = Mockito.mock(ShipmentServiceLayer.class);
        controller = new ShipmentController();
        controller.service = service;


        mockUser.setId(1L);
        mockUser.setName("Test");
        mockUser.setEmail("Test@junit.com");
        mockUser.setPassword("TestPassword");
        mockUser.setRole(Role.ADMIN);

        SessionManager.login(mockUser);
    }

    @Test
    public void getTest(){
        List<Shipment> mockShipments = Arrays.asList(new Shipment(), new Shipment());
        when(service.getAll()).thenReturn(mockShipments);
        ResponseEntity<?> response = controller.get();

        assertEquals(200,response.getStatusCodeValue());
        assertEquals(mockShipments,response.getBody());
    }

    @Test
    public void getByIdTest() throws ShipmentNotFoundException {
        Shipment mockShipment = new Shipment();
        mockShipment.setId(1L);

        when(service.getWithId(1L)).thenReturn(mockShipment);

        ResponseEntity<?> response = controller.getById(1L);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(mockShipment,response.getBody());
    }

    @Test
    public void addShipmentTest() throws ItemNotFoundException {
        mockUser.setRole(Role.SUPPLIER);
        ShipmentRequestDTO dto = new ShipmentRequestDTO();
        dto.setItemId(11L);

        Shipment mockShipment = new Shipment();
        mockShipment.setId(11L);

        when(service.add(dto)).thenReturn(mockShipment);

        ResponseEntity<?> response = controller.addShipment(dto);
        assertEquals(201,response.getStatusCodeValue());
        assertEquals(mockShipment,response.getBody());
    }

    @Test
    public void updateStatusTest() throws ShipmentNotFoundException {
        mockUser.setRole(Role.TRANSPORTER);
        Shipment mockShipment = new Shipment();

        when(service.updateStatus(1L, Status.IN_TRANSIT)).thenReturn(mockShipment);

        ResponseEntity<?> response = controller.updateStatus(1L,Status.IN_TRANSIT);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("Status updated for Shipment with id 1 as IN_TRANSIT",response.getBody());

    }

    @Test
    public void assignTransporterTest() throws ShipmentNotFoundException, TransporterNotFoundException {

        Shipment mockShipment = new Shipment();

        when(service.assign(1L, 10L)).thenReturn(mockShipment);

        ResponseEntity<?> response = controller.assignTransporter(1L,10L);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("Transporter updated for Shipment id 1 with transporter id 10" ,response.getBody());

    }
}
