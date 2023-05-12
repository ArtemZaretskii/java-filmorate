package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User add(User user);

    User update(User user);

    Map<Integer, User> getUsers();

    User getUserById(int id);

    Integer deleteUser(int id);

    User addFriend(int id, int friendId);

    Integer deleteFriend(int id, int friendId);

    Collection<User> getFriends(int id);

    Collection<User> getListOfCommonFriends(int id, int otherId);
}

