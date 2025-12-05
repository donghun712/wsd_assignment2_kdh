package com.example.bookstore.controller;

import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-error")
    public void testError() {
        throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @GetMapping("/test-success")
    public String testSuccess() {
        return "OK";
    }
}
