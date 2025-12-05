package com.example.bookstore.common.exception;

public enum ErrorCode {

    BAD_REQUEST(400, "BAD_REQUEST"),
    VALIDATION_FAILED(400, "VALIDATION_FAILED"),
    INVALID_QUERY_PARAM(400, "INVALID_QUERY_PARAM"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    TOKEN_EXPIRED(401, "TOKEN_EXPIRED"),
    FORBIDDEN(403, "FORBIDDEN"),
    BOOK_NOT_FOUND(404, "BOOK_NOT_FOUND"),
    RESOURCE_NOT_FOUND(404, "RESOURCE_NOT_FOUND"),
    USER_NOT_FOUND(404, "USER_NOT_FOUND"),
    DUPLICATE_RESOURCE(409, "DUPLICATE_RESOURCE"),
    STATE_CONFLICT(409, "STATE_CONFLICT"),
    UNPROCESSABLE_ENTITY(422, "UNPROCESSABLE_ENTITY"),
    TOO_MANY_REQUESTS(429, "TOO_MANY_REQUESTS"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    DATABASE_ERROR(500, "DATABASE_ERROR"),
    UNKNOWN_ERROR(500, "UNKNOWN_ERROR");

    private final int status;
    private final String code;

    ErrorCode(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
