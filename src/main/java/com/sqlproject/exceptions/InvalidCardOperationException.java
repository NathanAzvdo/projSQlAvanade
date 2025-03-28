package com.sqlproject.exceptions;

public class InvalidCardOperationException extends Exception {
    public InvalidCardOperationException(String message) {
        super(message);
    }
}