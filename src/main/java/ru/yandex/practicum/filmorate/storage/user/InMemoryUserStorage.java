package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
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

    @Override
    public User update(User user) {
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

    @Override
    public List<User> getUsers() {
        log.info("Выполнен запрос на получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (!users.containsKey(id)) {
            logAndMessageException("Пользователь не найден");
        }
        if (!users.containsKey(friendId)) {
            logAndMessageException("Пользователь не найден");
        }
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.info("Создана зависимость 'Друзья' между пользователями {} и {}",
                users.get(id).getName(),
                users.get(friendId).getName());
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        if (!users.containsKey(id)) {
            logAndMessageException("Пользователь 1 не найден");
        }
        if (!users.containsKey(friendId)) {
            logAndMessageException("Пользователь 2 не найден");
        }
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        log.info("Удалена зависимость 'Друзья' между пользователями {} и {}",
                users.get(id).getName(),
                users.get(friendId).getName());
    }

    @Override
    public List<User> getFriends(int id) {
        if (!users.containsKey(id)) {
            logAndMessageException("Пользователь не найден");
        }
        List<User> friends = new ArrayList<>();
        List<Integer> friendsId = new ArrayList<>(users.get(id).getFriends());
        for (int friendsIds : friendsId) {
            friends.add(users.get(friendsIds));
        }
        log.info("Количество зависимостей 'Друзья' у пользователя {} = {}",
                users.get(id).getName(),
                users.get(id).getFriends().size());
        return friends;
    }

    @Override
    public List<User> getListOfCommonFriends(int id, int otherId) {
        if (!users.containsKey(id)) {
            logAndMessageException("Пользователь 1 не найден");
        }
        if (!users.containsKey(otherId)) {
            logAndMessageException("Пользователь 2 не найден");
        }
        List<User> commonFriends = new ArrayList<>();
        for (int user : users.get(id).getFriends()) {
            if (users.get(otherId).getFriends().contains(user)) {
                commonFriends.add(users.get(user));
            }
        }
        log.info("Количество общих зависимостей 'Друзья' у пользователей {} и {}: {}",
                users.get(id).getName(),
                users.get(otherId).getName(),
                commonFriends.size());
        return commonFriends;
    }

    private void logAndMessageException(String message) {
        log.warn(message);
        throw new ValidationException(message);
    }
}
