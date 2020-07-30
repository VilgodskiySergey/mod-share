package com.vilgodskiy.modshare.application.exception;

public class AccessObjectException extends RuntimeException {

    public AccessObjectException(String msg) {
        super(msg);
    }

    public AccessObjectException(String msg, Throwable t) {
        super(msg, t);
    }
}