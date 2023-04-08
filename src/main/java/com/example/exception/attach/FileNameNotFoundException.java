package com.example.exception.attach;

public class FileNameNotFoundException extends RuntimeException{
    public FileNameNotFoundException(String message) {
        super(message);
    }
}
