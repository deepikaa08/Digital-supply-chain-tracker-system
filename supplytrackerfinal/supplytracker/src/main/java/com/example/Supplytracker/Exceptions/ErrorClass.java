package com.example.Supplytracker.Exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorClass {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorClass(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
