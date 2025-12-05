package com.example.bookstore.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String code;
    private String message;
    private Map<String, String> details;

    public ErrorResponse(LocalDateTime timestamp, String path, int status,
                         String code, String message, Map<String, String> details) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public static ErrorResponse of(ErrorCode errorCode, String message,
                                   String path, Map<String, String> details) {
        return new ErrorResponse(
                LocalDateTime.now(),
                path,
                errorCode.getStatus(),
                errorCode.getCode(),
                message,
                details
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return of(errorCode, message, path, null);
    }

    // getter들

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
