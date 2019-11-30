package com.gang.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode code) {
        this.message=code.getMessage();
    }

    public CustomizeException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
