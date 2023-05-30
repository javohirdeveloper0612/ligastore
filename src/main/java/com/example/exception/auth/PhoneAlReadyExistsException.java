package com.example.exception.auth;

public class PhoneAlReadyExistsException extends RuntimeException{
    public PhoneAlReadyExistsException(String message) {
        super(message);
    }
}
