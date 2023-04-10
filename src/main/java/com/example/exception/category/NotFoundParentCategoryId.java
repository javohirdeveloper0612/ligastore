package com.example.exception.category;

public class NotFoundParentCategoryId extends RuntimeException{

    public NotFoundParentCategoryId(String message) {
        super(message);
    }
}
