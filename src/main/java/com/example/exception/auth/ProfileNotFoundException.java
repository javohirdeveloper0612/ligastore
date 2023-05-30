package com.example.exception.auth;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String message) {
        super(message);
    }
}
