package com.example.supplytracker.ControllerTest;



import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.Controller.ReportController;
import com.example.supplytracker.DTO.DelayedShipmentDto;
import com.example.supplytracker.DTO.DeliveryPerformanceDto;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(Role.ADMIN);

        normalUser = new User();
        normalUser.setId(2L);
        normalUser.setRole(Role.SUPPLIER);
    }

    @Test
    void testGetDeliveryPerformance_NotLoggedIn() {
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(false);

            Object response = reportController.getDeliveryPerformance();
            assertEquals("Please login first", response);
        }
    }

    @Test
    void testGetDeliveryPerformance_NotAdmin() {
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(normalUser);

            Object response = reportController.getDeliveryPerformance();
            assertEquals("Access denied: You are not allowed to view this report.", response);
        }
    }

    @Test
    void testGetDeliveryPerformance_AsAdmin() {
        DeliveryPerformanceDto dto = new DeliveryPerformanceDto(10, 7, 3);
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(adminUser);
            when(reportService.getDeliveryPerformance()).thenReturn(dto);

            Object response = reportController.getDeliveryPerformance();
            assertEquals(dto, response);
        }
    }

    @Test
    void testGetDelayedShipments_NotLoggedIn() {
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(false);

            Object response = reportController.getDelayedShipments();
            assertEquals("Please login first", response);
        }
    }

    @Test
    void testGetDelayedShipments_NotAdmin() {
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(normalUser);

            Object response = reportController.getDelayedShipments();
            assertEquals("Access denied: You are not allowed to view this report.", response);
        }
    }

    @Test
    void testGetDelayedShipments_AsAdmin() {
        DelayedShipmentDto dto = new DelayedShipmentDto(1L, 100L, LocalDate.now(), LocalDateTime.now(), 2);
        List<DelayedShipmentDto> delayedShipments = List.of(dto);

        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(adminUser);
            when(reportService.getDelayedShipments()).thenReturn(delayedShipments);

            Object response = reportController.getDelayedShipments();
            assertEquals(delayedShipments, response);
        }
    }
}
