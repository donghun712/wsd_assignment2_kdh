
---

# ✅ **2️⃣ db-schema.md**

```md
# 📘 Database Schema – Bookstore Project

이 문서는 Bookstore 서비스의 DB 스키마 구조를 설명합니다.

---

# 1. users 테이블

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT PK | 사용자 ID |
| email | VARCHAR(255) UNIQUE | 이메일 |
| password | VARCHAR(255) | 암호화된 비밀번호 |
| name | VARCHAR(100) | 사용자 이름 |
| role | ENUM('ROLE_USER','ROLE_ADMIN') | 권한 |
| user_status | ENUM('ACTIVE','INACTIVE') | 사용자 상태 |
| phone | VARCHAR(20) | 연락처 |
| created_at | DATETIME | 생성일 |
| updated_at | DATETIME | 수정일 |

---

# 2. categories 테이블

| 컬럼 | 타입 | 설명 |
| id | BIGINT PK |
| name | VARCHAR(255) | 카테고리명 |

---

# 3. books 테이블

| 컬럼 | 타입 | 설명 |
| id | BIGINT PK |
| title | VARCHAR(255) |
| author | VARCHAR(255) |
| price | INT |
| stock | INT |
| category_id | BIGINT FK (categories.id) |
| average_rating | FLOAT |
| review_count | INT |
| created_at | DATETIME |
| updated_at | DATETIME |

---

# 4. reviews 테이블

| 컬럼 | 타입 | 설명 |
| id | BIGINT PK |
| book_id | BIGINT FK (books.id) |
| user_id | BIGINT FK (users.id) |
| content | TEXT |
| rating | INT |
| created_at | DATETIME |

---

# 5. orders 테이블

| 컬럼 | 타입 | 설명 |
| id | BIGINT PK |
| user_id | BIGINT FK |
| total_price | INT |
| status | ENUM('PENDING','PAID','SHIPPED','CANCELLED') |
| created_at | DATETIME |

---

# 6. order_items 테이블

| 컬럼 | 타입 | 설명 |
| id | BIGINT PK |
| order_id | BIGINT FK |
| book_id | BIGINT FK |
| quantity | INT |
| price | INT |

---

# ERD 요약

users (1) —— (N) reviews
users (1) —— (N) orders
orders (1) —— (N) order_items
books (1) —— (N) reviews
categories (1) —— (N) books

---