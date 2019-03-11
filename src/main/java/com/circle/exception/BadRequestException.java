package com.circle.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(Throwable t) {
        super("Bad request.", t);
    }
}
