package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logAndMessageException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        film.setId(id);
        films.put(id, film);
        id++;
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            logAndMessageException("Фильм не найден");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logAndMessageException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Выполнен запрос на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    private void logAndMessageException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }
}

