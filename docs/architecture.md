# 📘 Architecture Document – Bookstore Project

이 문서는 Bookstore 서비스의 전체 아키텍처 구조를 정리합니다.

---

# 1. Layered Architecture

Controller → Service → Repository → DB
↑
(Security / JWT / Exception Handling)

---

# 2. 주요 패키지 구조
com.example.bookstore
├─ auth
│ ├─ controller
│ ├─ dto
│ ├─ jwt
│
├─ user
│ ├─ controller
│ ├─ service
│ ├─ entity
│ ├─ repository
│
├─ book
│ ├─ controller
│ ├─ entity
│ ├─ repository
│
├─ review
│ ├─ controller
│ ├─ service
│ ├─ dto
│
├─ order
│ ├─ controller
│ ├─ service
│ ├─ entity
│
├─ admin
│ ├─ controller
│ ├─ dto
│
├─ common
│ ├─ response
│ ├─ exception
│ ├─ logging
│
├─ config


---

# 3. 인증/인가 구조

- JWT Access / Refresh Token
- Authorization Header → "Bearer <token>"
- Spring Security FilterChain
- RBAC (ROLE_USER, ROLE_ADMIN)

---

# 4. 예외 처리 구조

GlobalExceptionHandler →  
API 표준 응답 형태로 통일된 오류 처리 제공.

---

# 5. 로깅 구조

- RequestLoggingFilter  
- INFO / DEBUG / WARN 레벨 상세 출력  
- 개발/운영 분리 가능

---

# 6. 기술 스택

| 기술 | 사용 용도 |
|------|-----------|
| Spring Boot 3.4 | 애플리케이션 프레임워크 |
| Spring Security | JWT 인증/인가 |
| JPA / Hibernate | ORM |
| MySQL | 데이터베이스 |
| Flyway | DB 마이그레이션 |
| Postman | API 테스트 |
| Swagger | 문서화 |

---

