package com.sqlproject.exception;

public class BoardNotFoundException extends Exception {
    public BoardNotFoundException(String message) {
        super(message);
    }
}