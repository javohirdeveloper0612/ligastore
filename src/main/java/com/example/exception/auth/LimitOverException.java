package com.example.exception.auth;

public class LimitOverException extends RuntimeException{
    public LimitOverException(String message) {
        super(message);
    }
}
