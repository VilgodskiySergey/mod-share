package com.vilgodskiy.modshare.application.exception;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class AuthAccessDeniedException extends AuthApplicationException {

    public AuthAccessDeniedException() {
        super();
    }

    public AuthAccessDeniedException(String message) {
        super(message);
    }

    public AuthAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
