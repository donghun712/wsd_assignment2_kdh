# 📘 API Design Document – Bookstore Project

이 문서는 Bookstore 서비스의 전체 API 구조를 요약하여 설명합니다.

---

## ## API Root

---

# 1. Auth API

### **POST /api/auth/signup**
회원 가입

### **POST /api/auth/login**
로그인 + JWT 발급

### **POST /api/auth/refresh**
Access Token 재발급

---

# 2. User API

### **GET /api/user/me**
내 정보 조회

### **PUT /api/user/me**
내 정보 수정

### **POST /api/user/change-password**
비밀번호 변경

---

# 3. Admin User API (관리자 전용)

### **GET /api/admin/users**
전체 회원 조회 (페이지네이션)

### **GET /api/admin/users/{id}**
단일 회원 상세 조회

### **PATCH /api/admin/users/{id}/deactivate**
회원 비활성화

---

# 4. Books API

### **GET /api/books**
전체 도서 조회

### **GET /api/books/{id}**
도서 상세 조회

### **GET /api/books/category/{categoryId}**
카테고리별 조회

### **GET /api/books/search**
검색, 정렬, 페이지네이션 지원

### **GET /api/books/latest**
최신 도서 목록

### **GET /api/books/top-rated**
평점 상위 도서

---

# 5. Reviews API (로그인 필요)

### **GET /api/books/{bookId}/reviews**
특정 도서의 리뷰 조회

### **POST /api/books/{bookId}/reviews**
리뷰 작성

### **PATCH /api/books/{bookId}/reviews/{reviewId}**
리뷰 수정 (본인만)

### **DELETE /api/books/{bookId}/reviews/{reviewId}**
리뷰 삭제 (본인만)

---

# 6. Orders API

### **POST /api/orders**
주문 생성

### **GET /api/orders/my**
내 주문 목록 조회

### **GET /api/orders/my/{orderId}**
내 주문 상세 조회

---

# 7. Admin Orders API

### **GET /api/admin/orders**
전체 주문 목록 조회

### **GET /api/admin/orders/{id}**
주문 상세 조회

### **PATCH /api/admin/orders/{id}/status**
주문 상태 변경

---

# 8. Admin Stats API

### **GET /api/admin/stats/summary**
전체 통계

### **GET /api/admin/stats/users**
회원 상태 통계

---

# 9. System API

### **GET /health**
헬스체크

### **GET /test-success**
테스트용 응답

### **GET /test-error**
에러 테스트

---

# JWT 보안 구조

- Authorization: `Bearer <token>`
- Access Token 30분
- Refresh Token 7일
- ROLE_USER, ROLE_ADMIN 기반 RBAC

---

# 응답 규격 (공통)

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "OK",
  "payload": {...}
}
