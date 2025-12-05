package com.example.bookstore.common.response;

public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T payload;

    public ApiResponse(boolean success, String code, String message, T payload) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    // --------- 정적 메서드 (편의 메서드) ---------

    public static <T> ApiResponse<T> success(T payload) {
        return new ApiResponse<>(true, "SUCCESS", "OK", payload);
    }

    public static <T> ApiResponse<T> success(T payload, String code, String message) {
        return new ApiResponse<>(true, code, message, payload);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    // --------- getter (Lombok 대신 직접 작성) ---------

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getPayload() {
        return payload;
    }
}
