package com.example.supplytracker.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice    // Handles exceptions globally for all controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorClass> IdNotFound(ItemNotFoundException ex){
        ErrorClass error = new ErrorClass(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorClass> IdNotFound(ShipmentNotFoundException ex){
        ErrorClass error = new ErrorClass(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ErrorClass> UserNotFound(SupplierNotFoundException ex){
        ErrorClass error = new ErrorClass(HttpStatus.NOT_FOUND.value(),ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorClass> EmailAlreadyExists(EmailAlreadyExistsException ex){
        ErrorClass error = new ErrorClass(HttpStatus.ALREADY_REPORTED.value(), ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorClass> UserNotFound(UserNotFoundException ex){
        ErrorClass error = new ErrorClass(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransporterNotFoundException.class)
    public ResponseEntity<ErrorClass> TransporterNotFound(TransporterNotFoundException ex){
        ErrorClass error = new ErrorClass(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //MethodArgumentNotValidException, pre-defined exception, @Valid will handle this exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorClass> handleValidationException(MethodArgumentNotValidException ex){

        //creates a user-friendly message, so the client can understand
        String errMsg = ex.getBindingResult().getFieldErrors().stream().
                map(fieldError -> fieldError.getField()+": "+fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        //this takes the parameters, to show the error occurred in a good format
        ErrorClass error = new ErrorClass(
                HttpStatus.BAD_REQUEST.value(),
                errMsg,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
