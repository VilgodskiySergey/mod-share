package com.vilgodskiy.modshare.application.exception;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class AccessObjectException extends RuntimeException {

    public AccessObjectException(String msg) {
        super(msg);
    }

    public AccessObjectException(String msg, Throwable t) {
        super(msg, t);
    }
}