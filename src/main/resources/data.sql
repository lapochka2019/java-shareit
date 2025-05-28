DELETE FROM bookings;
DELETE FROM comments;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

INSERT INTO users (name, email)
VALUES
    ('Александр Петров', 'alex@example.com'),
    ('Екатерина Иванова', 'katya@example.com'),
    ('Дмитрий Смирнов', 'dmitry@example.com');

INSERT INTO requests (description, requestor_id)
VALUES ('Очень нужен советский радиоприемник! Помогите!', 3);

INSERT INTO items (name, description, is_available, user_id, request_id)
VALUES
    ('Палатка', 'Четырехместная палатка для путешествий', TRUE, 1, 1),
    ('Дрель', 'Мощная дрель, чтобы бесить соседей', TRUE, 1, 1),
    ('Советский радиоприемник', 'Нашла у бабушки на чердаке. Не работает', TRUE, 2, 1);