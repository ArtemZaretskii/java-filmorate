package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Repository
@Slf4j
public class RatingDao {
    private static final String SQL_GET_RATING_BY_ID = "SELECT id FROM rating WHERE id = ?";
    private static final String SQL_GET_ALL_RATING = "SELECT id FROM rating";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Rating getRatingById(Integer id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(SQL_GET_RATING_BY_ID, id);
        if (rowSet.next()) {
            log.info("Запрошен рейтинг {}.", id);
            return new Rating(rowSet.getInt("id"));
        }
        return new Rating(0);
    }

    public Collection<Rating> getRatings() {
        log.info("Запрошены все рейтинги.");
        return jdbcTemplate.query(SQL_GET_ALL_RATING, (rs, rowNum) -> new Rating(rs.getInt("id")));
    }
}