package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("/films")
@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms().values();
    }

    @DeleteMapping("/{id}")
    public Integer deleteFilm(@PathVariable int id) {
        return filmService.deleteFilm(id);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Integer addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Integer deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTop10Films(@RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getTop10Films(count);
    }
}

