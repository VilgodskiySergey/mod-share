package com.vilgodskiy.modshare.application.exception;

public class AuthExecutionConflictException extends AuthApplicationException {

    public AuthExecutionConflictException() {
        super();
    }

    public AuthExecutionConflictException(String message) {
        super(message);
    }

    public AuthExecutionConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
