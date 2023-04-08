package com.example.exception;

public class NotFoundParentCategory extends RuntimeException{

    public NotFoundParentCategory(String message) {
        super(message);
    }
}
