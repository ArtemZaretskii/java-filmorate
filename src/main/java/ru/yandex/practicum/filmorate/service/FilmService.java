package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public Film add(Film film) {
        log.info("Запрос на добавление фильма {}", film);
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("Запрос на обновление фильма {}", film);
        validateFilm(film);
        return filmStorage.update(film);
    }

    public List<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        log.info("Запрос на получение фильма с id='{}'", id);
        return filmStorage.getFilmById(id);
    }

    public Integer deleteFilm(int id) {
        if (id <= 0) {
            log.error("Пользователя с id='{}' не существует", id);
            throw new ObjectNotFoundException("Пользователь с id='" + id + "' не найден");
        }
        return filmStorage.deleteFilm(id);
    }

    public Integer addLike(int filmId, int userId) {
        if (userId <= 0) {
            log.error("Пользователя с id='{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь с id='" + userId + "' не найден");
        }
        return filmStorage.addLike(filmId, userId);
    }

    public Integer deleteLike(int filmId, int userId) {
        if (userId <= 0) {
            log.error("Пользователя с id='{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь с id='" + userId + "' не найден");
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрос на получение популярных фильмов фильмов");
        return filmStorage.getPopularFilms(count);
    }

}

