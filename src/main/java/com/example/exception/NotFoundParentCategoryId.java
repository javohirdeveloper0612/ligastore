package com.example.exception;

public class NotFoundParentCategoryId extends RuntimeException{

    public NotFoundParentCategoryId(String message) {
        super(message);
    }
}
