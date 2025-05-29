package com.example.supplytracker.Controller;

import com.example.supplytracker.Config.SessionManager;
import com.example.supplytracker.DTO.ItemRequestDTO;
import com.example.supplytracker.Entity.Item;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.SupplierNotFoundException;
import com.example.supplytracker.Service.ItemServiceLayer;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/items")
@Tag(name= "Items") // For Swagger
public class ItemController {

    @Autowired
    public ItemServiceLayer service;

    @GetMapping //To Fetch all Items
    public ResponseEntity<?> getItems() {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        User user = SessionManager.getCurrentUser();
        // can only be accessed by supplier and admin
        if (user.getRole() != Role.SUPPLIER && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Only SUPPLIERs can add items.");
        }
        return ResponseEntity.ok(service.getAllItems());
    }

    //TO add new item
    @PostMapping
    public ResponseEntity<?> addItem(@Valid @RequestBody ItemRequestDTO request) throws SupplierNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        // can only be accessed by supplier and admin
        if (user.getRole() != Role.SUPPLIER  && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Only SUPPLIERs can add items.");
        }

        Item item = service.add(request);
        return ResponseEntity.status(201).body(item);
    }

    //To fetch item by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable long id) throws ItemNotFoundException {
        if (!SessionManager.isLoggedIn()) {
            return ResponseEntity.status(401).body("Please log in first.");
        }

        User user = SessionManager.getCurrentUser();
        // can only be accessed by supplier and admin
        if (user.getRole() != Role.SUPPLIER && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Only SUPPLIERs can add items.");
        }

        Item item = service.getById(id);
        return ResponseEntity.ok(item);
    }
}
