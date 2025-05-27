package com.example.supplytracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ItemRequestDTO {

    @NotNull(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Category cannot be null")
    private String category;
    private long supplierId;


}
