package com.example.Supplytracker.ControllerTest;

import com.example.Supplytracker.Config.SessionManager;
import com.example.Supplytracker.Controller.ReportController;
import com.example.Supplytracker.DTO.DelayedShipmentDto;
import com.example.Supplytracker.DTO.DeliveryPerformanceDto;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.Role;
import com.example.Supplytracker.Service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportControllerTest {

    private ReportService reportService;
    private ReportController reportController;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        reportService = mock(ReportService.class);
        reportController = new ReportController(reportService);

        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(Role.ADMIN);

        normalUser = new User();
        normalUser.setId(2L);
        normalUser.setRole(Role.SUPPLIER);

        // Always clear session before tests
        SessionManager.logout();
    }

    @Test
    void testGetDeliveryPerformance_NotLoggedIn() {
        SessionManager.logout();  // simulate no login
        Object response = reportController.getDeliveryPerformance();
        assertEquals("Please login first", response);
    }

    @Test
    void testGetDeliveryPerformance_NotAdmin() {
        SessionManager.login(normalUser);  // simulate non-admin login
        Object response = reportController.getDeliveryPerformance();
        assertEquals("Access denied: You are not allowed to view this report.", response);
    }

    @Test
    void testGetDeliveryPerformance_AsAdmin() {
        DeliveryPerformanceDto dto = new DeliveryPerformanceDto(10, 7, 3);
        SessionManager.login(adminUser);  // simulate admin login

        when(reportService.getDeliveryPerformance()).thenReturn(dto);

        Object response = reportController.getDeliveryPerformance();
        assertEquals(dto, response);
    }

    @Test
    void testGetDelayedShipments_NotLoggedIn() {
        SessionManager.logout();  // simulate no login
        Object response = reportController.getDelayedShipments();
        assertEquals("Please login first", response);
    }

    @Test
    void testGetDelayedShipments_NotAdmin() {
        SessionManager.login(normalUser);  // simulate non-admin login
        Object response = reportController.getDelayedShipments();
        assertEquals("Access denied: You are not allowed to view this report.", response);
    }

    @Test
    void testGetDelayedShipments_AsAdmin() {
        DelayedShipmentDto dto = new DelayedShipmentDto(1L, 100L, LocalDate.now(), LocalDateTime.now(), 2);
        SessionManager.login(adminUser);  // simulate admin login

        when(reportService.getDelayedShipments()).thenReturn(List.of(dto));

        Object response = reportController.getDelayedShipments();
        assertEquals(List.of(dto), response);
    }
}
