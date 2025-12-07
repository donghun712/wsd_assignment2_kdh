package com.example.bookstore.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> details;

    // GlobalExceptionHandler에서 직접 new 할 때 쓰는 생성자
    public ErrorResponse(
            LocalDateTime timestamp,
            String path,
            HttpStatus httpStatus,
            String code,
            String message,
            Map<String, String> details
    ) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = httpStatus.value();
        this.code = code;
        this.message = message;
        this.details = details;
    }

    // ✅ GlobalExceptionHandler 에서 사용하는 정적 팩토리 메소드 1
    //   ErrorResponse.of(errorCode, path, method, details)
    public static ErrorResponse of(
            ErrorCode errorCode,
            String path,
            String httpMethod,
            Map<String, String> details
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                path,
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                details
        );
    }

    // ✅ GlobalExceptionHandler 에서 사용하는 정적 팩토리 메소드 2
    //   ErrorResponse.of(errorCode, path, method)
    public static ErrorResponse of(
            ErrorCode errorCode,
            String path,
            String httpMethod
    ) {
        return of(errorCode, path, httpMethod, null);
    }

    // ======= getter =======

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
