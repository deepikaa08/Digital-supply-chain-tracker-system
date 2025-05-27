package com.example.supplytrackersystem.Controller;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping("/delivery-performance")
    public Object getDeliveryPerformance() {
        if (!SessionManager.isLoggedIn()) {
            return "Please login first";
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.ADMIN ) {
            return "Access denied: You are not allowed to view this report.";
        }

        return service.getDeliveryPerformance();
    }

    @GetMapping("/delayed-shipments")
    public Object getDelayedShipments() {
        if (!SessionManager.isLoggedIn()) {
            return "Please login first";
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.ADMIN ) {
            return "Access denied: You are not allowed to view this report.";
        }

        return service.getDelayedShipments();
    }
}
