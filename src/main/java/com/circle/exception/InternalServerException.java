package com.circle.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(Throwable t) {
        super("Something went wrong...", t);
    }
}
