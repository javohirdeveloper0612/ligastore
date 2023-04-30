package com.example.exception.auth;

public class SmsTimeOverException extends RuntimeException{
    public SmsTimeOverException(String message) {
        super(message);
    }
}
