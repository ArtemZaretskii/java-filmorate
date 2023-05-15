package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM GENRES",
                new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre findById(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE ID = ?",
                    new BeanPropertyRowMapper<>(Genre.class), id);
        } catch (DataAccessException ex) {
            log.error("Жанра с id='{}' не существует", id);
            throw new ObjectNotFoundException("Genre with id='" + id + "' not found");
        }
    }
}