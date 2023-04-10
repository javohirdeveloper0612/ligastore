package com.example.exception.attach;

public class AttachNotFoundException extends RuntimeException{
    public AttachNotFoundException(String message) {
        super(message);
    }
}
