package com.hiperium.city.tasks.api.exception;

public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final String errorMessageKey;
    private final transient Object[] args;

    protected ApplicationException(String errorCode, String errorMessageKey, Object... args) {
        super();
        this.errorCode = errorCode;
        this.errorMessageKey = errorMessageKey;
        this.args = args;
    }

    protected ApplicationException(Exception exception, String errorCode, String errorMessageKey, Object... args) {
        super(exception.getMessage(), exception);
        this.errorCode = errorCode;
        this.errorMessageKey = errorMessageKey;
        this.args = args;
    }

    protected String getErrorCode() {
        return errorCode;
    }

    protected String getErrorMessageKey() {
        return errorMessageKey;
    }

    protected Object[] getArgs() {
        return args;
    }
}