package com.example.bookstore.common.exception;

import java.util.Map;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, String> details;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.details = null;
    }

    public BusinessException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.details = details;
    }

    // 🔥 추가해야 하는 부분
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.getCode() + " - " + message);
        this.errorCode = errorCode;
        this.details = Map.of("message", message);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}

