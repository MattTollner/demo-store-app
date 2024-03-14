
#Password password
INSERT INTO store_user (password, email, first_name, last_name, phone_number, email_verified)
VALUES
('$2a$10$4u2T2Lgo5zu/hoD9I8uw1uVJqY4s3L0N2H.3WFAUGWS7UjvbdhCB.', 'user1@example.com', 'user1-FN', 'user1-LN', 'user1-123-456-7890', true);

INSERT INTO store_user (password, email, first_name, last_name, phone_number, email_verified)
VALUES
    ('$2a$10$4u2T2Lgo5zu/hoD9I8uw1uVJqY4s3L0N2H.3WFAUGWS7UjvbdhCB.', 'user2@example.com', 'user2-FN', 'user2-LN', 'user2-123-456-7890', false);