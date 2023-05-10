package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        validateUser(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getListOfCommonFriends(int id1, int id2) {
        return userStorage.getListOfCommonFriends(id1, id2);
    }
}

