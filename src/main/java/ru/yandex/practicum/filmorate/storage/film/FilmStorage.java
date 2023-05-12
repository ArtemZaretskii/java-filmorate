package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    Map<Integer, Film> getFilms();

    Integer deleteFilm(Integer id);

    Film getFilmById(int id);

    Integer addLike(int filmId, int userId);

    Integer deleteLike(int filmId, int userId);

    Collection<Film> getTop10Films(int count);
}

