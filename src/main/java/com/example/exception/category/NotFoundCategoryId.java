package com.example.exception.category;

public class NotFoundCategoryId extends RuntimeException{

    public NotFoundCategoryId(String message) {
        super(message);
    }

}
