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

@ExtendWith(MockitoExtension.class)   // Tells JUnit to enable Mockito
class ReportControllerTest {

    @Mock
    private ReportService reportService;  // Mocking the service used by the controller

    @InjectMocks
    private ReportController reportController;  // Inject mock service into the controller

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        // Create an admin user
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(Role.ADMIN);
        // Create a normal supplier user
        normalUser = new User();
        normalUser.setId(2L);
        normalUser.setRole(Role.SUPPLIER);
    }

    @Test
    void testGetDeliveryPerformance_NotLoggedIn() {
        // Simulate user not logged in
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(false);
            // Call the API and check the response
            Object response = reportController.getDeliveryPerformance();
            assertEquals("Please login first", response);
        }
    }

    @Test
    void testGetDeliveryPerformance_NotAdmin() {
        // Simulate a logged-in user who is not an admin
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(normalUser);
            // Expect access denied
            Object response = reportController.getDeliveryPerformance();
            assertEquals("Access denied: You are not allowed to view this report.", response);
        }
    }

    @Test
    void testGetDeliveryPerformance_AsAdmin() {
        // Simulate a logged-in admin user
        DeliveryPerformanceDto dto = new DeliveryPerformanceDto(10, 7, 3);
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(adminUser);
            when(reportService.getDeliveryPerformance()).thenReturn(dto);
            // Expect the service to return the correct report
            Object response = reportController.getDeliveryPerformance();
            assertEquals(dto, response);
        }
    }

    @Test
    void testGetDelayedShipments_NotLoggedIn() {
        // Simulate user not logged in
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(false);
            // Expect login message
            Object response = reportController.getDelayedShipments();
            assertEquals("Please login first", response);
        }
    }

    @Test
    void testGetDelayedShipments_NotAdmin() {
        // Simulate a normal user who is logged in
        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(normalUser);
            // Expect access denied
            Object response = reportController.getDelayedShipments();
            assertEquals("Access denied: You are not allowed to view this report.", response);
        }
    }

    @Test
    void testGetDelayedShipments_AsAdmin() {
        // Simulate admin viewing delayed shipments
        DelayedShipmentDto dto = new DelayedShipmentDto(1L, 100L, LocalDate.now(), LocalDateTime.now(), 2);
        List<DelayedShipmentDto> delayedShipments = List.of(dto);

        try (MockedStatic<SessionManager> mocked = mockStatic(SessionManager.class)) {
            mocked.when(SessionManager::isLoggedIn).thenReturn(true);
            mocked.when(SessionManager::getCurrentUser).thenReturn(adminUser);
            when(reportService.getDelayedShipments()).thenReturn(delayedShipments);
            // Expect the delayed shipments to be returned
            Object response = reportController.getDelayedShipments();
            assertEquals(delayedShipments, response);
        }
    }
}
