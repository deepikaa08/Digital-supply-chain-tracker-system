package com.example.supplytracker.Controller;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.DTO.CheckpointLogDTO;
import com.example.supplytracker.Entity.CheckpointLog;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Service.CheckpointLogService;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
@Tag(name= "CheckpointLog")
public class CheckpointLogController {

    private final CheckpointLogService checkpointLogService;

    @PostMapping
    public ResponseEntity<?> addCheckpoint(@RequestBody CheckpointLogDTO dto) {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        if (user.getRole() != Role.TRANSPORTER) {
            return ResponseEntity.status(403).body("Only TRANSPORTERs can add checkpoints.");
        }

        return ResponseEntity.ok(checkpointLogService.addCheckpoint(dto));
    }

    @GetMapping("/shipment/{id}")
    public ResponseEntity<?> getByShipment(@PathVariable Shipment id) {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        List<CheckpointLog> logs = checkpointLogService.getByShipmentId(id);
        return ResponseEntity.ok(logs);
    }
}
