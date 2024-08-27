package com.bookstore.api.BookstoreAPI.ErrorHandling;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
