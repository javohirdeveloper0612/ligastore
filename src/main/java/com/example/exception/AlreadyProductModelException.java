package com.example.exception;

public class AlreadyProductModelException extends RuntimeException {
    public AlreadyProductModelException(String message) {
        super(message);
    }
}
