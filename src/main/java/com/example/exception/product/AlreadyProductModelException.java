package com.example.exception.product;

public class AlreadyProductModelException extends RuntimeException {
    public AlreadyProductModelException(String message) {
        super(message);
    }
}
