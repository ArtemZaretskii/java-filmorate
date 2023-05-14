package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
@Slf4j
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendDao;

    @Override
    public List<User> getUsers() {
        List<User> users = jdbcTemplate.query("SELECT * FROM USERS", new BeanPropertyRowMapper<>(User.class));
        return users;
    }

    @Override
    public User add(User user) {
        List<User> userList = jdbcTemplate.query("SELECT * FROM USERS WHERE LOGIN = ?",
                new BeanPropertyRowMapper<>(User.class), user.getLogin());

        if (!userList.isEmpty()) {
            log.error("Пользователя с login='{}' уже существует", user.getLogin());
            throw new ValidationException("User with login='" + user.getLogin() + "' already exist");
        }

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, kh);

        final Integer userId = kh.getKeyAs(Integer.class);
        user.setId(userId);

        log.info("Добавлен новый пользователь {}.", user);
        return user;
    }

    @Override
    public User update(User user) {

        int update = jdbcTemplate.update("UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE ID = ?",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());

        if (update == 0) {
            log.error("Пользователя с id='{}' не существует", user.getId());
            throw new ObjectNotFoundException("User with id='" + user.getId() + "' not found");
        }

        log.info("Обновлен пользователь {}.", user.getId());
        return user;
    }

    @Override
    public Integer deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM USERS WHERE ID = ?", id);
        log.debug("Удален пользователь {}", id);
        return id;
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?",
                    new BeanPropertyRowMapper<>(User.class), id);
        } catch (DataAccessException ex) {
            log.error("Пользователя с id='{}' не существует", id);
            throw new ObjectNotFoundException("User with id='" + id + "' not found");
        }
    }

    @Override
    public void addFriend(int id, int friendId) {
        checkExistUserById(id);
        checkExistUserById(friendId);
        friendDao.addFriend(id, friendId);

    }

    @Override
    public void deleteFriend(int id, int friendId) {
        checkExistUserById(id);
        checkExistUserById(friendId);
        friendDao.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getFriends(int id) {
        checkExistUserById(id);
        return friendDao.getFriends(id);
    }

    @Override
    public Collection<User> getListOfCommonFriends(int id, int otherId) {
        checkExistUserById(id);
        checkExistUserById(otherId);

        return friendDao.getListOfCommonFriends(id, otherId);
    }

    private void checkExistUserById(Integer id) {
        boolean result = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT COUNT(ID) > 0 FROM USERS WHERE ID = ?",
                Boolean.class, id));

        if (!result) {
            log.error("Пользователя с id='{}' не существует", id);
            throw new ObjectNotFoundException("User with id='" + id + "' not found");
        }
    }

}