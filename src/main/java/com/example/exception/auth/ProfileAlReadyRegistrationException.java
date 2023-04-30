package com.example.exception.auth;

public class ProfileAlReadyRegistrationException extends RuntimeException{
    public ProfileAlReadyRegistrationException(String message) {
        super(message);
    }
}
