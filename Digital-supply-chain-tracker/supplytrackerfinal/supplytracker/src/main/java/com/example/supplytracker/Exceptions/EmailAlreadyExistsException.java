package com.example.supplytracker.Exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
