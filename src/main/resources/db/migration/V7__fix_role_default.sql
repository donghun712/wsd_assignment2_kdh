-- V2__fix_role_default.sql

-- 기존 잘못된 기본값 수정
ALTER TABLE users
    MODIFY role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER';

-- 혹시 남아 있을지 모르는 잘못된 값도 한 번에 교정
UPDATE users
SET role = 'ROLE_USER'
WHERE role = 'USER';

UPDATE users
SET role = 'ROLE_ADMIN'
WHERE role = 'ADMIN';
