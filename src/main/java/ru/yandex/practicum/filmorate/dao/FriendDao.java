package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public void addFriend(Integer id, Integer friendId) {
        jdbcTemplate.update(
                "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, 0)",
                id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        jdbcTemplate.update(
                "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?",
                id, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}.", id, friendId);
    }

    public Collection<User> getFriends(Integer id) {
        log.info("Запрошен список друзей пользователя {}.", id);
        return jdbcTemplate.query(
                "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                        "FROM FRIENDS f " +
                        "LEFT JOIN USERS U ON f.FRIEND_ID = U.ID " +
                        "WHERE USER_ID = ?",
                new BeanPropertyRowMapper<>(User.class), id);
    }

    public Collection<User> getListOfCommonFriends(Integer id, Integer otherId) {
        log.info("Запрошен список друзей пользователя {}.", id);
        return jdbcTemplate.query(
                "SELECT * FROM USERS us\n" +
                        "JOIN FRIENDS AS fr1 ON us.ID = fr1.FRIEND_ID\n" +
                        "JOIN FRIENDS AS fr2 ON us.ID = fr2.FRIEND_ID\n" +
                        "WHERE fr1.USER_ID = ? AND fr2.USER_ID = ?",
                new BeanPropertyRowMapper<>(User.class), id, otherId);
    }

}