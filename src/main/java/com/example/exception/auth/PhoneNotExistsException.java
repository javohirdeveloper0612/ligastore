package com.example.exception.auth;

public class PhoneNotExistsException extends RuntimeException{
    public PhoneNotExistsException(String message) {
        super(message);
    }
}
