package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User add(User user) {
        log.info("Запрос на добавление пользователя {}", user);
        validateUser(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        log.info("Запрос на обновление пользователя {}", user);
        validateUser(user);
        return userStorage.update(user);
    }

    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        log.info("Запрос на получение пользователя с id='{}'", id);
        return userStorage.getUserById(id);
    }

    public Integer deleteUser(Integer id) {
        log.info("Запрос на удаление пользователя с id='{}'", id);
        return userStorage.deleteUser(id);
    }

    public void addFriend(int id, int friendId) {
        log.info("Пользователь с id='{}' добавил в друзья пользователя с id='{}'", id, friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        log.info("Пользователь с id='{}' удалил из друзей пользователя с id='{}'", id, friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public Collection<User> getFriends(int id) {
        log.info("Запрос на получение друзей пользователя с id='{}'", id);
        return userStorage.getFriends(id);
    }

    public Collection<User> getListOfCommonFriends(int id, int otherId) {
        log.info("Запрос на получение общих друзей между пользователем с id='{}' и пользователем с id='{}'", id, otherId);
        return userStorage.getListOfCommonFriends(id, otherId);
    }
}

