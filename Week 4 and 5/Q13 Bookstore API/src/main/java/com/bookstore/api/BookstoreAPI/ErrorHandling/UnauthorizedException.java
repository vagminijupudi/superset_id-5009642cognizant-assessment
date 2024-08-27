package com.bookstore.api.BookstoreAPI.ErrorHandling;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
