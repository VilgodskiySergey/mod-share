package com.vilgodskiy.modshare.application.exception;

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