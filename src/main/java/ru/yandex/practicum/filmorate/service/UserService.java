package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User add(User user) {
        validateUser(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Integer deleteUser(Integer id) {
        return userStorage.deleteUser(id);
    }

    public User addFriend(int id, int friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public Integer deleteFriend(int id, int friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

    public Collection<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public Collection<User> getListOfCommonFriends(int id, int otherId) {
        return userStorage.getListOfCommonFriends(id, otherId);
    }
}

