package com.example.exception.category;

public class NotFoundParentCategory extends RuntimeException{

    public NotFoundParentCategory(String message) {
        super(message);
    }
}
