package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            logAndMessageObjectNotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        log.info("Выполнен запрос на получение списка пользователей");
        return users;
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            logAndMessageObjectNotFoundException("Пользователь не найден");
        }
        log.info("Выполнен запрос на получения пользователя по id");
        return users.get(id);
    }

    @Override
    public Integer deleteUser(int id) {
        users.remove(id);
        log.info("Удален пользователь {}", id);
        return id;
    }

    @Override
    public User addFriend(int id, int friendId) {
        if (!users.containsKey(id)) {
            logAndMessageObjectNotFoundException("Пользователь не найден");
        }
        if (!users.containsKey(friendId)) {
            logAndMessageObjectNotFoundException("Пользователь не найден");
        }
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.info("Создана зависимость 'Друзья' между пользователями {} и {}",
                users.get(id).getName(),
                users.get(friendId).getName());
        return getUserById(friendId);
    }

    @Override
    public Integer deleteFriend(int id, int friendId) {
        if (!users.containsKey(id)) {
            logAndMessageObjectNotFoundException("Пользователь 1 не найден");
        }
        if (!users.containsKey(friendId)) {
            logAndMessageObjectNotFoundException("Пользователь 2 не найден");
        }
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        log.info("Удалена зависимость 'Друзья' между пользователями {} и {}",
                users.get(id).getName(),
                users.get(friendId).getName());
        return null;
    }

    @Override
    public Collection<User> getFriends(int id) {
        if (!users.containsKey(id)) {
            logAndMessageObjectNotFoundException("Пользователь не найден");
        }
        List<User> friends = new ArrayList<>();
        Set<Integer> friendsId = users.get(id).getFriends();
        for (int friendsIds : friendsId) {
            friends.add(users.get(friendsIds));
        }
        log.info("Количество зависимостей 'Друзья' у пользователя {} = {}",
                users.get(id).getName(),
                users.get(id).getFriends().size());
        return friends;
    }

    @Override
    public Collection<User> getListOfCommonFriends(int id, int otherId) {
        if (!users.containsKey(id)) {
            logAndMessageObjectNotFoundException("Пользователь 1 не найден");
        }
        if (!users.containsKey(otherId)) {
            logAndMessageObjectNotFoundException("Пользователь 2 не найден");
        }
        List<User> commonFriends = new ArrayList<>(getFriends(id).stream()
                .distinct()
                .filter(getFriends(otherId)::contains)
                .collect(Collectors.toList()));
        log.info("Количество общих зависимостей 'Друзья' у пользователей {} и {}: {}",
                users.get(id).getName(),
                users.get(otherId).getName(),
                commonFriends.size());
        return commonFriends;
    }

    private void logAndMessageObjectNotFoundException(String message) {
        log.warn(message);
        throw new ObjectNotFoundException(message);
    }
}

