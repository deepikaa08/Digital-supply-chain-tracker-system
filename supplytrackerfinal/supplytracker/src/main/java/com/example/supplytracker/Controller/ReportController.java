package com.example.supplytracker.Controller;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.DTO.DelayedShipmentDto;
import com.example.supplytracker.DTO.DeliveryPerformanceDto;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Service.ReportService;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/reports") // All APIs in this class will start with /api/reports
@Tag(name= "Reports")  // For Swagger UI - this groups the APIs under "Reports"
public class ReportController {

    @Autowired
    private ReportService service;   // Injects the ReportService to call business logic

    @GetMapping("/delivery-performance")  // API: GET /api/reports/delivery-performance
    public Object getDeliveryPerformance() {
        // Check if user is logged in
        if (!SessionManager.isLoggedIn()) {
            return "Please login first";
        }

        // Get the currently logged-in user
        User user = SessionManager.getCurrentUser();
        // Allow only ADMIN users to access the report
        if (user.getRole() != Role.ADMIN ) {
            return "Access denied: You are not allowed to view this report.";
        }
        // Call service to get delivery performance report
        return service.getDeliveryPerformance();
    }

    @GetMapping("/delayed-shipments")  // API: GET /api/reports/delayed-shipments
    public Object getDelayedShipments() {
        // Check if user is logged in
        if (!SessionManager.isLoggedIn()) {
            return "Please login first";
        }
        // Get the currently logged-in user
        User user = SessionManager.getCurrentUser();
        // Allow only ADMIN users to access the report
        if (user.getRole() != Role.ADMIN ) {
            return "Access denied: You are not allowed to view this report.";
        }
        // Call service to get delayed shipments report
        return service.getDelayedShipments();
    }
}
