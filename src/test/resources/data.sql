INSERT INTO store_user (password, email, first_name, last_name, phone_number, email_verified)
VALUES
('$2a$10$4u2T2Lgo5zu/hoD9I8uw1uVJqY4s3L0N2H.3WFAUGWS7UjvbdhCB.', 'user1@example.com', 'user1-FN', 'user1-LN', 'user1-123-456-7890', true),
('$2a$10$4u2T2Lgo5zu/hoD9I8uw1uVJqY4s3L0N2H.3WFAUGWS7UjvbdhCB.', 'user2@example.com', 'user2-FN', 'user2-LN', 'user2-123-456-7890', false),
('$2a$10$4u2T2Lgo5zu/hoD9I8uw1uVJqY4s3L0N2H.3WFAUGWS7UjvbdhCB.', 'user3@example.com', 'user3-FN', 'user3-LN', 'user3-123-456-7890', true);



INSERT INTO role (role_name)
VALUES
('ADMIN'),
('CUSTOMER');


INSERT INTO store_user_role (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 2);


INSERT INTO product_category (category_name)
VALUES
('RAM'),
('CPUS');


INSERT INTO product (product_name, price, product_description, product_category)
VALUES
('RAM-1', 100, 'RAM-1 description', 1),
('RAM-2', 200, 'RAM-2 description', 1),
('CPU-1', 300, 'CPU-1 description', 2),
('CPU-2', 400, 'CPU-2 description', 2);


INSERT INTO image (img_url, product_id)
VALUES
('https://picsum.photos/id/237/200/300', 1),
('https://picsum.photos/id/238/200/300', 1),
('https://picsum.photos/id/239/200/300', 2),
('https://picsum.photos/id/240/200/300', 2),
('https://picsum.photos/id/241/200/300', 3),
('https://picsum.photos/id/242/200/300', 3),
('https://picsum.photos/id/243/200/300', 4),
('https://picsum.photos/id/244/200/300', 4);

INSERT INTO inventory (stock, product_id)
VALUES
(10, 1),
(30, 2),
(50, 3),
(70, 4);


INSERT INTO address (address_line_1, address_line_2, city, postcode, user_id)
VALUES
('address_line_1', 'address_line_2', 'city', 'postcode', 1),
('address_line_1_user_3', 'address_line_2_user_3', 'city_user3', 'postcode_user3', 3);

INSERT INTO store_order (is_paid, total_price, user_id, address_id, created_at)
VALUES
(true, 500, 1, 1,'2022-01-01 00:00:00'),
(true, 2500, 1, 2,'2022-05-05 00:00:00'),
(true, 2500, 3, 2,'2023-01-01 00:00:00'),
(false, 100, 3, 2,'2023-01-01 00:00:00');

INSERT INTO store_order_quantities (quantity,price, store_order_id, product_id)
VALUES
(1, 100, 1, 1),
(2, 200, 1, 2),
(3, 300, 2, 3),
(4, 400, 2, 4),
(3, 300, 3, 3),
(4, 400, 3, 4),
(1, 100, 4, 4);

INSERT INTO shopping_cart (user_id)
VALUES
(3);

INSERT INTO shopping_cart_quantities (quantity, product_id, shopping_cart_id)
VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1);

