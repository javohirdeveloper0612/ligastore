package com.example.exception.auth;

public class ProfileBlockedException extends RuntimeException{
    public ProfileBlockedException(String message) {
        super(message);
    }
}
