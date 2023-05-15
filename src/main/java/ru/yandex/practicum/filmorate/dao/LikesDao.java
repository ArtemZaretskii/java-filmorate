package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LikesDao {

    private final JdbcTemplate jdbcTemplate;

    public Integer addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes(user_id, film_id) VALUES (?, ?)", userId, filmId);
        log.info("Пользователь {} поставил like фильму {}", userId, filmId);
        return userId;
    }

    public Integer deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);
        log.debug("Пользователь {} удалил like фильму {}", userId, filmId);
        return userId;
    }

}