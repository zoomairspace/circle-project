package com.circle.exception;

/**
 * Custom BadRequestException, 400
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(Throwable t) {
        super("Bad request.", t);
    }
}
