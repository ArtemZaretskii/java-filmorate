package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public Film add(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Integer deleteFilm(int id) {
        return filmStorage.deleteFilm(id);
    }

    public Integer addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public Integer deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getTop10Films(int count) {
        return filmStorage.getTop10Films(count);
    }

}

