# Bookstore REST API 프로젝트
Spring Boot 기반 온라인 서점 백엔드 서비스

## 📌 프로젝트 개요
본 프로젝트는 **온라인 서점(Bookstore) REST API 서버**로,  
회원 관리, 도서 조회, 리뷰, 주문, 관리자 기능을 포함한 **웹 서비스 백엔드** 구현을 목표로 합니다.

### 주요 기능
- 사용자 회원가입 / 로그인 / JWT 인증
- 사용자 정보 조회 및 수정
- 관리자 전용 사용자 관리
- 도서 조회, 검색, 정렬, 추천
- 리뷰 CRUD
- 주문 생성 및 조회
- 관리자용 주문 관리 및 통계 API
- 레이트리밋 / 로깅 / 에러 핸들링 / Swagger 문서화
- GitHub Actions 기반 CI 구성
  main 브랜치 기준 push / PR 시 자동으로 ./gradlew test, ./gradlew build 수행
---

## 🚀 실행 방법

### 1) 로컬 실행
```bash
# 프로젝트 클론
git clone <your-repo-url>
cd wsd_assignment2_kdh

# 빌드
./gradlew clean build

# 애플리케이션 실행
java -jar build/libs/bookstore.jar
```

### 2) JCloud/서버 실행
```bash
nohup java -jar bookstore.jar --spring.profiles.active=prod > app.log 2>&1 &
```

---

## 🔐 환경변수 설명 (`.env.example`)
```env
DB_URL=jdbc:mysql://localhost:3306/bookstore
DB_USERNAME=bookuser
DB_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret_here
```

> 실제 `.env`는 제출 파일로 GitHub에는 올리지 않았습니다.

---

## 🌐 배포 주소
| 항목 | URL |
|------|-----|
| Base URL | http://113.198.66.68:10027/ |
| API Root | http://113.198.66.68:10027/api |
| Swagger | http://113.198.66.68:10027/swagger-ui.html |
| Health Check | http://113.198.66.68:10027/health |

---

## 🔑 인증 플로우 (JWT)
1. `POST /api/auth/login` 로그인
2. Access Token + Refresh Token 발급
3. 인증 필요한 요청 시 `Authorization: Bearer <TOKEN>` 헤더 사용
4. Access Token 만료 시 `/api/auth/refresh` 로 재발급

---

## 👥 역할/권한 표

- **ROLE_USER**: 리뷰, 주문, 사용자 정보 기능 사용 가능  
- **ROLE_ADMIN**: 관리자 API 모든 기능 사용 가능

---

## 🧪 예제 계정
| 구분 | 이메일 | 비밀번호 |
|------|--------|----------|
| 일반 사용자 | justuser@example.com | passwordhere |
| 관리자 | admin1@example.com | passwordhere |

---

## DB 연결 정보
Host: 113.198.66.68
Port: 13306
Database: bookstore
Username: bookuser
Password: <제출용 문서에만 기입, GitHub에는 공백>
접속 명령어:
mysql -h 113.198.66.68 -P 13306 -u bookuser -p

---

## 🧪 자동화 테스트

JUnit5 + Spring Boot Test 기반

컨트롤러 단위 테스트 + 통합 테스트 포함

총 21개 테스트 케이스 작성 (성공/실패 케이스 모두 포함)

---

## 🔧 CI 구성(GitHub Actions)

본 프로젝트는 GitHub Actions를 이용하여 **자동 빌드 파이프라인(CI)**을 구성했습니다.
테스트는 로컬 환경에서 이미 모두 통과하였으며,
GitHub Actions 환경에서는 DB 미구성으로 인해 테스트가 실패할 수 있어 테스트를 제외하고(build-only) 자동화하도록 설정했습니다.

즉, CI의 목적은 코드 변경 시 자동 빌드 검증이며,
테스트 실행은 로컬 환경에서 수행하는 방식으로 분리하여 안정성을 확보했습니다.

---

## 📚 엔드포인트 요약표

총 32개의 엔드포인트 구현

### Auth
```
POST /api/auth/signup
POST /api/auth/login
POST /api/auth/refresh
```

### User
```
GET /api/user/me
PUT /api/user/me
POST /api/user/change-password
```

### Admin User
```
GET /api/admin/users
GET /api/admin/users/{id}
PATCH /api/admin/users/{id}/deactivate
```

### Books
```
GET /api/books
GET /api/books/{id}
GET /api/books/category/{categoryId}
GET /api/books/search
GET /api/books/recommendations
GET /api/books/latest
GET /api/books/top-rated
```

### Reviews
```
GET /api/books/{bookId}/reviews
POST /api/books/{bookId}/reviews
PATCH /api/books/{bookId}/reviews/{reviewId}
DELETE /api/books/{bookId}/reviews/{reviewId}
```

### Orders
```
POST /api/orders
GET /api/orders/my
GET /api/orders/my/{orderId}
```

### Admin Orders
```
GET /api/admin/orders
GET /api/admin/orders/{orderId}
PATCH /api/admin/orders/{orderId}/status
```

### Admin Stats
```

GET /api/admin/stats/summary
GET /api/admin/stats/users
```
### Admin Books
```
GET /api/admin/books

```

### System
```
GET /health
GET /test-success
GET /test-error
```

---

## ⚙️ 성능 / 보안 고려사항
- JWT 기반 인증/인가 적용
- 레이트리밋 필터 적용 (IP 기반 요청 제한)
- N+1 문제 완화를 위한 `default_batch_fetch_size` 적용
- Request/Response 로깅 필터 추가
- 입력 검증 기반 Validation

---

## 📌 한계 및 개선 계획
- 주문 상태 변경 시 알림 기능 추가 가능
- 추천 도서 알고리즘 고도화
- 캐싱을 통한 도서 조회 성능 향상
- 통합 테스트 확장
- 테스트로 인해 북id 4~203번이 존재하지 않습니다.

---

## 📁 Postman Collection
`postman/Bookstore_api_kdh.json` 파일 포함 → 전체 엔드포인트 테스트 가능

구성:
- Auth
- User
- Books
- Reviews
- Orders
- Admin User
- Admin Orders
- Admin Stats
- Admin Books
- System

토큰 자동 저장 스크립트 포함.

---

# 📑 Additional Documents

`docs/` 폴더 포함:

| 파일 | 설명 |
|------|------|
| api-design.md | 전체 REST API 명세 |
| db-schema.md | DB 테이블 스키마 및 ERD |
| architecture.md | 프로젝트 아키텍처 |

---

