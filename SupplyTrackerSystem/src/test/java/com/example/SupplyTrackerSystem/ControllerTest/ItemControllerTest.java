package com.example.supplytracker.ControllerTest;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.Controller.ItemController;
import com.example.supplytracker.DTO.ItemRequestDTO;
import com.example.supplytracker.Entity.Item;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.SupplierNotFoundException;
import com.example.supplytracker.Service.ItemServiceLayer;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class ItemControllerTest {

    private SessionManager sessionManager;
    private ItemController controller;
    private ItemServiceLayer service;

    @BeforeEach
    public void setup() {
        service = Mockito.mock(ItemServiceLayer.class);
        controller = new ItemController();
        controller.service = service; //injected mocked service

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Test");
        mockUser.setEmail("Test@junit.com");
        mockUser.setPassword("TestPassword");
        mockUser.setRole(Role.ADMIN);

        SessionManager.login(mockUser);
    }

    @Test
    public void getItemsTest() {

        List<Item> mockItems = Arrays.asList(new Item(), new Item());
        when(service.getAllItems()).thenReturn(mockItems);

        ResponseEntity<?> response = controller.getItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockItems, response.getBody());
    }

    @Test
    public void addItemTest() throws SupplierNotFoundException {

        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setName("Book");
        dto.setCategory("Stationary");
        dto.setSupplierId(1L);
        Item mockItem = new Item();
        mockItem.setName("Book");
        when(service.add(dto)).thenReturn(mockItem);
        ResponseEntity<?> response = controller.addItem(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockItem,response.getBody());

    }

    @Test
    //get item by id
    public void getItemTest() throws ItemNotFoundException {
        Item mockItem = new Item();
        mockItem.setId(11L);

        when(service.getById(11l)).thenReturn(mockItem);

        ResponseEntity<?> response = controller.getItem(11L);

        assertEquals(200,response.getStatusCodeValue());
        assertEquals(mockItem,response.getBody());
    }


}
