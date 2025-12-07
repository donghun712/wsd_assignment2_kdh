package com.example.bookstore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 기본 설정
 *
 * - /v3/api-docs
 * - /swagger-ui/index.html
 * 로 문서 확인 가능
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bookstore REST API",
                description = "웹설 과제2 - 도서 쇼핑몰 백엔드 API 문서입니다.\n\n" +
                        "JWT 기반 인증 / 사용자 / 도서 / 주문 / 리뷰 / 관리자 API를 포함합니다.",
                version = "v1.0",
                contact = @Contact(
                        name = "김동훈",
                        email = "example@example.com"
                )
        ),
        servers = {
                @Server(
                        description = "로컬 서버",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // 별도 Bean 없어도 되고, 어노테이션만으로 설정 끝.
}
