package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    List<Film> getFilms();

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getTop10Films(int count);
}
