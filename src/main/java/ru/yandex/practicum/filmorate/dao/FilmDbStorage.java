package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String SQL_GET_FILMS = "SELECT * FROM films";
    private static final String SQL_GET_LIKES = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String SQL_GET_GENRES = "SELECT genre_id FROM film_genre WHERE film_id = ?";
    private static final String SQL_ADD_FILM = "INSERT INTO films(name, description, release_date, duration, rating) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_GET_FILM_ID = "SELECT film_id FROM films WHERE name = ? AND " +
            "description = ? AND release_date = ? AND duration = ? AND rating = ?";
    private static final String SQL_ADD_GENRE = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
    private static final String SQL_UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, rating = ? WHERE film_id = ?";
    private static final String SQL_DELETE_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String SQL_UPDATE_GENRE = "INSERT INTO film_genre(film_id, genre_id) VALUES(?, ?)";
    private static final String SQL_DELETE_FILM = "DELETE FROM films WHERE film_id = ?";
    private static final String SQL_GET_FILM = "SELECT * FROM films AS f LEFT JOIN likes AS l ON f.film_id = " +
            "l.film_id WHERE f.film_id = ? GROUP BY f.film_id, l.likes_id";
    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> filmsMap = new HashMap<>();
        List<Film> filmList = jdbcTemplate.query(SQL_GET_FILMS, (rs, rowNum) -> makeFilm(rs));
        for (Film f : filmList) {
            filmsMap.put(f.getId(), f);
        }
        log.info("Запрошен список всех фильмов.");
        return filmsMap;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int rating = rs.getInt("rating");
        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(SQL_GET_LIKES, (rs1, rowNum1) ->
                (rs1.getInt("user_id")), id));
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(SQL_GET_GENRES, (rs2, rowNum) ->
                (new Genre(rs2.getInt("genre_id"))), id));
        if (genres.isEmpty()) {
            return new Film(id, name, description, releaseDate, duration, likes, new Rating(rating), null);
        }
        return new Film(id, name, description, releaseDate, duration, likes, new Rating(rating), genres);
    }

    @Override
    public Film add(Film film) {
        jdbcTemplate.update(SQL_ADD_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRating().getId());
        log.info("Добавлен новый фильм {}.", film);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(SQL_GET_FILM_ID, film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRating().getId());
        if (filmRows.next()) {
            if (film.getGenres() == null) {
                return new Film(filmRows.getInt("film_id"), film.getName(), film.getDescription(),
                        film.getReleaseDate(), film.getDuration(), new HashSet<>(), film.getRating(), null);
            } else {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(SQL_ADD_GENRE, filmRows.getInt("film_id"), genre);
                }
                return new Film(filmRows.getInt("film_id"), film.getName(), film.getDescription(),
                        film.getReleaseDate(), film.getDuration(), film.getRating(), film.getGenres());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRating().getId(), film.getId());
        log.info("Обновлен фильм {}.", film.getId());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            jdbcTemplate.update(SQL_DELETE_GENRE, film.getId());
            return film;
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(SQL_DELETE_GENRE, film.getId());
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(SQL_UPDATE_GENRE, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Integer deleteFilm(Integer id) {
        jdbcTemplate.update(SQL_DELETE_FILM, id);
        jdbcTemplate.update(SQL_DELETE_GENRE, id);
        log.info("Удален фильм {}", id);
        return id;
    }

    @Override
    public Film getFilmById(int id) {
        return jdbcTemplate.query(SQL_GET_FILM, (rs, rowNum) -> makeFilm(rs), id).get(0);
    }

    @Override
    public Integer addLike(int filmId, int userId) {
        return likesDao.addLike(filmId, userId);
    }

    @Override
    public Integer deleteLike(int filmId, int userId) {
        return likesDao.addLike(filmId, userId);
    }

    @Override
    public Collection<Film> getTop10Films(int count) {
        return likesDao.getTop10Films(count);
    }

}