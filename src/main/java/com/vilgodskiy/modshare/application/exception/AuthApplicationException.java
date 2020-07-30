package com.vilgodskiy.modshare.application.exception;

public class AuthApplicationException extends RuntimeException {

    public AuthApplicationException() {
        super();
    }

    public AuthApplicationException(String message) {
        super(message);
    }

    public AuthApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
