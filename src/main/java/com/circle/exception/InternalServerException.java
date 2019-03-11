package com.circle.exception;

/**
 * Custom 500 error
 */

public class InternalServerException extends RuntimeException {

    public InternalServerException(Throwable t) {
        super("Something went wrong...", t);
    }
}
