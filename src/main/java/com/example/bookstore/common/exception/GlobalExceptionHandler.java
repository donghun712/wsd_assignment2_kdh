package com.example.bookstore.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1) 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();

        // 로그 남기기 (비즈니스 예외는 WARN 정도로)
        log.warn("[BUSINESS] {} {} - code: {}, details: {}",
                request.getMethod(),
                request.getRequestURI(),
                errorCode.getCode(),
                ex.getDetails()
        );

        // 여기서 'message' 자리는 ErrorCode의 code 또는 ex.getMessage() 등으로 채우면 됨
        ErrorResponse body = ErrorResponse.of(
                errorCode,
                errorCode.getCode(),          // ❗ errorCode.getMessage() 안 씀
                request.getRequestURI(),
                ex.getDetails()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(body);
    }

    // 2) @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> details = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("[VALIDATION] {} {} - {}", 
                request.getMethod(),
                request.getRequestURI(),
                details
        );

        ErrorResponse body = ErrorResponse.of(
            ErrorCode.VALIDATION_FAILED,
            "요청 값 검증에 실패했습니다.",
            request.getRequestURI(),
            details
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 3) JSON 파싱 오류 등
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        log.warn("[BAD_REQUEST_BODY] {} {} - {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.BAD_REQUEST,
                "요청 본문을 읽을 수 없습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 4) 그 밖의 모든 예외 (예상 못 한 서버 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        // ❗ ex.printStackTrace() 대신 log.error로 스택트레이스를 남김
        log.error("[ERROR] {} {} - Unexpected server error",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
