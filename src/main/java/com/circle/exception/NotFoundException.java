package com.circle.exception;

/**
 * Custom 404
 */

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
