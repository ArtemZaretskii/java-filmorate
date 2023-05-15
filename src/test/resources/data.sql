MERGE INTO mpa (id, name) VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO genres (id, name) VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

INSERT INTO users(email, login, name, birthday)
VALUES ('mail', 'login', 'name', '1990-03-08');

INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('The Rock', 'Starring Nicolas Cage and Sean Connery', '1996-06-07', '136', '1');