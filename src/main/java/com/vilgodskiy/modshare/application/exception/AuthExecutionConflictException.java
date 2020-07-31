package com.vilgodskiy.modshare.application.exception;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
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
