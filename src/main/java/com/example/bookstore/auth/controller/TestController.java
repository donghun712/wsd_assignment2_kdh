package com.example.bookstore.auth.controller;

import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test API", description = "개발/테스트용 엔드포인트 (운영 기능과는 별도)")
public class TestController {

    @GetMapping("/test-error")
    @Operation(
            summary = "에러 응답 테스트",
            description = "강제로 BusinessException을 발생시켜 에러 응답 형식을 테스트합니다."
    )
    public void testError() {
        throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @GetMapping("/test-success")
    @Operation(
            summary = "성공 응답 테스트",
            description = "간단한 문자열 OK를 반환하는 성공 응답 테스트용 엔드포인트입니다."
    )
    public String testSuccess() {
        return "OK";
    }
}
