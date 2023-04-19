package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logAndMessageException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            logAndMessageException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            logAndMessageException("Дата рождения не может быть в будущем");
        }
        user.setId(id);
        users.put(id, user);
        id++;
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logAndMessageException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (!users.containsKey(user.getId())) {
            logAndMessageException("Пользователь не найден");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            logAndMessageException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            logAndMessageException("Дата рождения не может быть в будущем");
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Выполнен запрос на получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    private void logAndMessageException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }
}
