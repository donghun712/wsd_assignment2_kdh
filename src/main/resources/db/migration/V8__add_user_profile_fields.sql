ALTER TABLE users
    ADD COLUMN phone_number VARCHAR(20) NULL AFTER name,
    ADD COLUMN address TEXT NULL AFTER phone_number,
    ADD COLUMN birth_date DATE NULL AFTER address,
    ADD COLUMN gender VARCHAR(10) NULL AFTER birth_date,
    ADD COLUMN region VARCHAR(100) NULL AFTER gender,
    ADD COLUMN user_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' AFTER role;
