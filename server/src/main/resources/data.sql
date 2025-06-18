DELETE FROM bookings;
DELETE FROM comments;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

INSERT INTO users (name, email)
VALUES
('Алексей Иванов', 'ivanov@example.com'),
('Мария Петрова', 'petrova@example.com'),
('Сергей Смирнов', 'smirnov@example.com'),
('Ольга Кузнецова', 'kuznetsova@example.com');

INSERT INTO requests (description, requester_id, creation_date)
VALUES
('Хочу найти чемодан на неделю', 2, TIMESTAMP '2025-06-18 10:00:00'),
('Хочу найти дрель на неделю', 4, TIMESTAMP '2025-06-18 12:00:00');

INSERT INTO items (name, description, is_available, user_id, request_id)
VALUES
('Чемодан', 'Большой чемодан на 4 колесах', TRUE, 1, 1),
('Велосипед', 'Горный велосипед Trek', TRUE, 3, NULL);

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status)
VALUES
(TIMESTAMP '2025-06-16 10:00:00', TIMESTAMP '2025-06-16 20:00:00', 1, 2, 'APPROVED'),
(TIMESTAMP '2025-06-17 10:00:00', TIMESTAMP '2025-06-17 20:00:00', 1, 3, 'WAITING'),
(TIMESTAMP '2025-06-18 10:00:00', TIMESTAMP '2025-06-18 15:00:00', 2, 4, 'APPROVED');

INSERT INTO comments (text, created, item_id, author_id)
VALUES
('Отличный велосипед!', '2025-06-18 12:00:00', 2, 4);