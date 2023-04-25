package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(User user);

    List<User> getUsers();

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriends(int id);

    List<User> getListOfCommonFriends(int id1, int id2);
}
