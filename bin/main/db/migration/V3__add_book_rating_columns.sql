ALTER TABLE books
    ADD COLUMN average_rating DOUBLE DEFAULT 0 AFTER category_id,
    ADD COLUMN review_count INT DEFAULT 0 AFTER average_rating;
