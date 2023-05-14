package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final UserStorage userStorage;
    private int id = 1;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film add(Film film) {
        film.setId(id);
        films.put(id, film);
        id++;
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            logAndMessageObjectNotFoundException("Фильм не найден");
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
            logAndMessageObjectNotFoundException("Фильм не найден");
        }
        log.info("Выполнен запрос на получение фильма по id");
        return films.get(id);
    }

    @Override
    public Integer deleteFilm(Integer id) {
        films.remove(id);
        log.info("Удален фильм {}.", id);
        return id;
    }

    @Override
    public Integer addLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            logAndMessageObjectNotFoundException("Фильм не найден");
        }
        User user = userStorage.getUserById(userId);
        if (films.get(filmId).getLikes().contains(userId)) {
            logAndMessageValidationException("Пользователь уже поставил лайк этому фильму");
        }
        films.get(filmId).getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}",
                user.getName(),
                films.get(filmId).getName());
        return userId;
    }

    @Override
    public Integer deleteLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            logAndMessageObjectNotFoundException("Фильм не найден");
        }
        User user = userStorage.getUserById(userId);
        if (!films.get(filmId).getLikes().contains(userId)) {
            logAndMessageValidationException("Пользователь не ставил лайк этому фильму");
        }
        films.get(filmId).getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк фильму {}",
                user.getName(),
                films.get(filmId).getName());
        return userId;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.info("Выполнен запрос на получение топ 10 фильмов");
        return films.values().stream().sorted((i0, i1) -> {
            int comp = Integer.compare(i0.getLikes().size(), i1.getLikes().size());
            return comp * -1;
        }).limit(count).collect(Collectors.toList());
    }


    private void logAndMessageValidationException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }

    private void logAndMessageObjectNotFoundException(String message) {
        log.warn(message);
        throw new ObjectNotFoundException(message);
    }
}

