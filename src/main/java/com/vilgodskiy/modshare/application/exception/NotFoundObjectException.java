package com.vilgodskiy.modshare.application.exception;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class NotFoundObjectException extends RuntimeException {

    public NotFoundObjectException() {
        super();
    }

    public NotFoundObjectException(String msg) {
        super(msg);
    }

    public NotFoundObjectException(String msg, Throwable t) {
        super(msg, t);
    }
}