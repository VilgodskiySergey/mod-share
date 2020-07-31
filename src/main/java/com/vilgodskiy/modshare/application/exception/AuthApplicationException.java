package com.vilgodskiy.modshare.application.exception;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
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
