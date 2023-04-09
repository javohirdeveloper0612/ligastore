package com.example.controller;

import com.example.exception.EmptyListException;
import com.example.exception.NotFoundParentCategory;
import com.example.exception.NotMatchException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.attach.AttachNotFoundException;
import com.example.exception.attach.FileNameNotFoundException;
import com.example.exception.attach.FileUploadException;
import com.example.exception.attach.OriginalFileNameNullException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatusCode status,
                                 WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = new LinkedList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({AttachNotFoundException.class})
    private ResponseEntity<?> handler(AttachNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({NotFoundParentCategory.class})
    private ResponseEntity<?> handler(NotFoundParentCategory e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({FileNameNotFoundException.class})
    private ResponseEntity<?> handler(FileNameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({FileNotFoundException.class})
    private ResponseEntity<?> handler(FileNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({FileUploadException.class})
    private ResponseEntity<?> handler(FileUploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({OriginalFileNameNullException.class})
    private ResponseEntity<?> handler(OriginalFileNameNullException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({NotMatchException.class})
    private ResponseEntity<?> handler(NotMatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    private ResponseEntity<?> handler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


    @ExceptionHandler({ProductNotFoundException.class})
    private ResponseEntity<?> handler(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({EmptyListException.class})
    private ResponseEntity<?> handler(EmptyListException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}