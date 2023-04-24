package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            logAndMessageException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            logAndMessageException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logAndMessageException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            logAndMessageException("Продолжительность фильма должна быть положительной");
        }
        film.setId(id);
        films.put(id, film);
        id++;
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            logAndMessageException("Фильм не найден");
        }
        if (film.getName().isBlank()) {
            logAndMessageException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            logAndMessageException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logAndMessageException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            logAndMessageException("Продолжительность фильма должна быть положительной");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Выполнен запрос на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    private void logAndMessageException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }
}
