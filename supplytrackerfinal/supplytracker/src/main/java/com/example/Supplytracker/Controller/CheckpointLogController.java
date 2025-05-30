package com.example.Supplytracker.Controller;

import com.example.Supplytracker.Config.SessionManager;
import com.example.Supplytracker.DTO.CheckpointLogDTO;
import com.example.Supplytracker.Entity.CheckpointLog;
import com.example.Supplytracker.Entity.Shipment;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.Role;
import com.example.Supplytracker.Service.CheckpointLogService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
@Tag(name= "CheckpointLog") // Swagger documentation tag
public class CheckpointLogController {

    private final CheckpointLogService checkpointLogService;

    // Endpoint to add a checkpoint log
    @PostMapping
    public ResponseEntity<?> addCheckpoint(@RequestBody CheckpointLogDTO dto) {
        // Check if user is logged in
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        // Get current user and check if they are a TRANSPORTER
        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.TRANSPORTER) {
            return ResponseEntity.status(403).body("Only TRANSPORTERs can add checkpoints.");
        }

        // Add the checkpoint and return the result
        return ResponseEntity.ok(checkpointLogService.addCheckpoint(dto));
    }

    // Endpoint to get all checkpoints for a specific shipment
    @GetMapping("/shipment/{id}")
    public ResponseEntity<?> getByShipment(@PathVariable Shipment id) {
        // Check if user is logged in
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        // Retrieve checkpoint logs for the given shipment
        List<CheckpointLog> logs = checkpointLogService.getByShipmentId(id);
        return ResponseEntity.ok(logs);
    }
}
