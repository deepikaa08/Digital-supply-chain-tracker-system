package com.example.supplytracker.ServiceTest;

import com.example.supplytracker.DTO.ShipmentRequestDTO;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.ShipmentNotFoundException;
import com.example.supplytracker.Exceptions.TransporterNotFoundException;
import com.example.supplytracker.Repository.ItemRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import com.example.supplytracker.Repository.UserRepo;
import com.example.supplytracker.Service.ShipmentServiceLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ShipmentServiceLayerTest {

    private UserRepo userrepo;                // Mocked User repository
    private ItemRepository itemrepo;          // Mocked Item repository
    private ShipmentRepository shipmentrepo;  // Mocked Shipment repository
    private ShipmentServiceLayer service;     // Service under test

    @BeforeEach
    public void setup() {
        // Initialize mocks before each test
        itemrepo = Mockito.mock(ItemRepository.class);
        userrepo = Mockito.mock(UserRepo.class);
        shipmentrepo = Mockito.mock(ShipmentRepository.class);

        // Inject mocks into service
        service = new ShipmentServiceLayer();
        service.itemrepo = itemrepo;
        service.shipmentrepo = shipmentrepo;
        service.userrepo = userrepo;
    }

    @Test
    public void getAllTest() {
        // Arrange: Prepare mock data
        List<Shipment> mockShipments = Arrays.asList(new Shipment(), new Shipment());
        when(shipmentrepo.findAll()).thenReturn(mockShipments);

        // Act: Call service method
        List<Shipment> result = service.getAll();

        // Assert: Check if returned list matches mock data
        assertEquals(mockShipments, result);
    }

    @Test
    public void getWithIdTest() throws ShipmentNotFoundException {
        // Arrange
        Shipment mockItem = new Shipment();
        mockItem.setShipmentId(11L);
        when(shipmentrepo.findById(11L)).thenReturn(Optional.of(mockItem));

        // Act
        Shipment result = service.getWithId(11L);

        // Assert
        assertEquals(mockItem, result);
    }

    @Test
    public void updateStatus() throws ShipmentNotFoundException {
        // Arrange
        Shipment mockShipment = new Shipment();
        mockShipment.setShipmentId(11L);
        mockShipment.setCurrentStatus(Status.IN_TRANSIT);
        when(shipmentrepo.findById(11L)).thenReturn(Optional.of(mockShipment));

        Shipment updatedShipment = new Shipment();
        updatedShipment.setShipmentId(11L);
        updatedShipment.setCurrentStatus(Status.DELAYED);
        when(shipmentrepo.save(mockShipment)).thenReturn(updatedShipment);

        // Act
        Shipment result = service.updateStatus(11L, Status.DELAYED);

        // Assert
        assertEquals(updatedShipment, result);
    }

    @Test
    public void addTest() throws ItemNotFoundException {
        // Arrange
        ShipmentRequestDTO dto = new ShipmentRequestDTO();
        dto.setItemId(11L);

        Shipment mockShipment = new Shipment();
        mockShipment.setShipmentId(11L);

        when(itemrepo.existsById(11L)).thenReturn(true);
        when(shipmentrepo.save(any(Shipment.class))).thenReturn(mockShipment);

        // Act
        Shipment result = service.add(dto);

        // Assert
        assertEquals(mockShipment, result);
    }

    @Test
    public void assignTest() throws ShipmentNotFoundException, TransporterNotFoundException {
        // Arrange
        Shipment mockShipment = new Shipment();
        mockShipment.setShipmentId(11L);

        User mockTransporter = new User();
        mockTransporter.setId(20L);

        Shipment updatedShipment = new Shipment();
        updatedShipment.setShipmentId(11L);
        updatedShipment.setTransporterId(20L);

        when(shipmentrepo.findById(11L)).thenReturn(Optional.of(mockShipment));
        when(userrepo.findById(20L)).thenReturn(Optional.of(mockTransporter));
        when(shipmentrepo.save(mockShipment)).thenReturn(updatedShipment);

        // Act
        Shipment result = service.assign(11L, 20L);

        // Assert
        assertEquals(updatedShipment, result);
    }
}
