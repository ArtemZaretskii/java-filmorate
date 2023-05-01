package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public final class ValidationService {

    private ValidationService() {
    }

    public static void validateUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logAndMessageValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            logAndMessageValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            logAndMessageValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static void validateFilm(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            logAndMessageValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            logAndMessageValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logAndMessageValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            logAndMessageValidationException("Продолжительность фильма должна быть положительной");
        }

    }

    private static void logAndMessageValidationException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }

}
