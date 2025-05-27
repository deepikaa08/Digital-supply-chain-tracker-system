package com.example.supplytracker.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String category;

    //private User Supplier;
    private long supplierId;
    private LocalDate createdDate = LocalDate.now();

    public Item(String name, String category, long supplierId, LocalDate createdDate) {
        this.name = name;
        this.category = category;
        this.supplierId = supplierId;
        this.createdDate = createdDate;
    }

    public Item() { }


}
