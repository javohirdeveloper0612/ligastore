package com.example.exception.attach;

public class FileUploadException extends RuntimeException{
    public FileUploadException(String message) {
        super(message);
    }
}
