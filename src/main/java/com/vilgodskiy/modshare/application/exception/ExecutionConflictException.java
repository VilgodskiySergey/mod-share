package com.vilgodskiy.modshare.application.exception;

import lombok.Getter;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Getter
public class ExecutionConflictException extends RuntimeException {

    private ErrorMessage errMsg;

    public ExecutionConflictException(String msg) {
        super(msg);
        errMsg = new ErrorMessage().addError(msg);
    }

    public ExecutionConflictException(ErrorMessage errMsg) {
        super();
        this.errMsg = errMsg;
    }
}