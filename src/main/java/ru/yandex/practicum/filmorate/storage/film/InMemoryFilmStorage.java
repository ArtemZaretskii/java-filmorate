package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private UserStorage userStorage;

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

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            logAndMessageException("Фильм не найден");
        }
        log.info("Выполнен запрос на получение фильма по id");
        return films.get(id);
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (!films.containsKey(filmId)) {
            logAndMessageException("Фильм не найден");
        }
        if (!userStorage.getUsers().contains(userId)) {
            logAndMessageException("Пользователь не найден");
        }
        if (films.get(filmId).getLikes().contains(userId)) {
            logAndMessageException("Пользователь уже поставил лайк этому фильму");
        }
        films.get(filmId).getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}",
                userStorage.getUsers().get(userId).getName(),
                films.get(filmId).getName());
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (!films.containsKey(filmId)) {
            logAndMessageException("Фильм не найден");
        }
        if (!userStorage.getUsers().contains(userId)) {
            logAndMessageException("Пользователь не найден");
        }
        if (!films.get(filmId).getLikes().contains(userId)) {
            logAndMessageException("Пользователь не ставил лайк этому фильму");
        }
        films.get(filmId).getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк фильму {}",
                userStorage.getUsers().get(userId).getName(),
                films.get(filmId).getName());
    }

    @Override
    public List<Film> getTop10Films(int count) {
        log.info("Выполнен запрос на получение топ 10 фильмов");
        return getFilms().stream().sorted(Comparator.comparingInt(
                film -> film.getLikes().size())).limit(count).collect(Collectors.toList());
    }

    private void logAndMessageException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }
}
