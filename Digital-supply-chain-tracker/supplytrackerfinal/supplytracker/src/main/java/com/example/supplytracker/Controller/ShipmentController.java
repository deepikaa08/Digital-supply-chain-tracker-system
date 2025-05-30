package com.example.supplytracker.Controller;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.DTO.ShipmentRequestDTO;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.ShipmentNotFoundException;
import com.example.supplytracker.Exceptions.TransporterNotFoundException;
import com.example.supplytracker.Service.ShipmentServiceLayer;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipment") // For Swagger documentation
public class ShipmentController {

    @Autowired
    public ShipmentServiceLayer service;

    // Endpoint to fetch all shipments (requires login)
    @GetMapping
    public ResponseEntity<?> get() {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        return ResponseEntity.ok(service.getAll());
    }

    // Endpoint to fetch a shipment by its ID (requires login)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) throws ShipmentNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        Shipment shipment = service.getWithId(id);
        return ResponseEntity.ok(shipment);
    }

    // Endpoint to add a new shipment (only accessible by SUPPLIER role)
    @PostMapping
    public ResponseEntity<?> addShipment(@Valid @RequestBody ShipmentRequestDTO request) throws ItemNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.SUPPLIER) {
            return ResponseEntity.status(403).body("Only SUPPLIERs can add shipments.");
        }

        return ResponseEntity.status(201).body(service.add(request));
    }

    // Endpoint to update the status of a shipment (only accessible by TRANSPORTER role)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable long id, @RequestParam Status status) throws ShipmentNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.TRANSPORTER) {
            return ResponseEntity.status(403).body("Only TRANSPORTERs can update status.");
        }

        Shipment shipment = service.updateStatus(id, status);
        String message = "Status updated for Shipment with id " + id + " as " + status;
        return ResponseEntity.ok(message);
    }

    // Endpoint to assign a transporter to a shipment (only accessible by ADMIN role)
    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTransporter(@PathVariable long id, @RequestParam long transporterId) throws ShipmentNotFoundException, TransporterNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Only ADMINs can assign transporters.");
        }

        Shipment shipment = service.assign(id, transporterId);
        String message = "Transporter updated for Shipment id " + id + " with transporter id " + transporterId;
        return ResponseEntity.ok(message);
    }
}
