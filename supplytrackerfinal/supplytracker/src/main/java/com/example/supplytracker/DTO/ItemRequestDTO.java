package com.example.supplytracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ItemRequestDTO {

    // Item name must not be null
    @NotNull(message = "Name cannot be blank")
    private String name;
    // Item Category must not be null
    @NotNull(message = "Category cannot be null")
    private String category;
    private long supplierId;


}
