package com.example.exception.product;

public class NotMatchException extends RuntimeException {
    public NotMatchException(String message) {
        super(message);
    }
}
