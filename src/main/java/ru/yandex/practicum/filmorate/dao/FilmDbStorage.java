package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;

    private final FilmMapper filmMapper;

    @Override
    public Film add(Film film) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, kh);

        final Integer filmId = kh.getKeyAs(Integer.class);
        film.setId(filmId);

        log.info("Добавлен новый фильм {}.", film);

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                        filmId, genre.getId());
            }
            setGenresForFilmsByIds(Map.of(filmId, film));
        }

        Set<Integer> likes = film.getLikes();
        if (likes != null) {
            for (Integer userId : likes) {
                jdbcTemplate.update("INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)", userId, filmId);
            }
            setLikesByFilmsIds(Map.of(filmId, film));
        }

        if (film.getMpa() != null) {
            Mpa mpa = jdbcTemplate.queryForObject(
                    "SELECT m.ID, m.NAME FROM MPA m Left JOIN FILMS f ON f.MPA_ID = m.ID WHERE f.ID = ?",
                    new BeanPropertyRowMapper<>(Mpa.class), film.getId());
            film.setMpa(mpa);
        }
        return film;
    }


    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query(
                "SELECT F.*, M.NAME as mpa_name FROM FILMS F LEFT JOIN MPA M on M.ID = F.MPA_ID",
                filmMapper);

        setGenresAndLikesForFilms(films);
        log.info("Запрошен список всех фильмов.");
        return films;
    }

    @Override
    public Film update(Film film) {
        boolean isExistFilm = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT COUNT(ID) > 0 FROM FILMS WHERE ID = ?",
                Boolean.class, film.getId()));

        if (!isExistFilm) {
            throw new ObjectNotFoundException("Film with id='" + film.getId() + "' not found");
        }

        int update = jdbcTemplate.update(
                "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, MPA_ID = ? " +
                        "WHERE ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        if (update == 0) {
            return null;
        }

        if (film.getGenres() != null) {
            updateGenres(film);
        }

        if (film.getLikes() != null) {
            updateLikes(film);
        }

        log.info("Обновлен фильм {}.", film.getId());
        Mpa mpa = jdbcTemplate.queryForObject(
                "SELECT m.ID, m.NAME FROM MPA m Left JOIN FILMS f ON f.MPA_ID = m.ID WHERE f.ID = ?",
                new BeanPropertyRowMapper<>(Mpa.class), film.getId());
        film.setMpa(mpa);
        film.setGenres(getGenresById(film.getId()));
        return film;
    }

    @Override
    public Integer deleteFilm(Integer id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE ID = ?", id);
        log.info("Удален фильм {}", id);
        return id;
    }

    @Override
    public Film getFilmById(int id) {
        List<Film> films = jdbcTemplate.query(
                "SELECT F.*, M.NAME as mpa_name FROM FILMS F LEFT JOIN MPA M on M.ID = F.MPA_ID WHERE F.ID = ?",
                filmMapper, id);

        if (films.isEmpty()) {
            throw new ObjectNotFoundException("Film with id='" + id + "' not found");
        }

        setGenresAndLikesForFilms(List.of(films.get(0)));

        return films.get(0);
    }

    @Override
    public Integer addLike(int filmId, int userId) {
        return likesDao.addLike(filmId, userId);
    }

    @Override
    public Integer deleteLike(int filmId, int userId) {
        return likesDao.deleteLike(filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        if (Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT COUNT(USER_ID) <= 0 FROM LIKES",
                Boolean.class))) {
            List<Film> films = jdbcTemplate
                    .query("SELECT f.*, M.NAME as mpa_name " +
                                    "FROM FILMS F " +
                                    "LEFT JOIN MPA M on M.ID = f.MPA_ID " +
                                    "ORDER BY F.ID DESC " +
                                    "LIMIT ?",
                            filmMapper, count);

            setGenresAndLikesForFilms(films);
            return films;
        }
        List<Film> films = jdbcTemplate
                .query(
                        "SELECT f.*, COUNT(USER_ID) as count_like, m.NAME as mpa_name " +
                                "FROM LIKES l " +
                                "LEFT JOIN FILMS f ON l.FILM_ID = f.ID " +
                                "LEFT JOIN MPA m on M.ID = F.MPA_ID " +
                                "GROUP BY l.FILM_ID " +
                                "ORDER BY count_like DESC " +
                                "LIMIT ?",
                        filmMapper, count);

        setGenresAndLikesForFilms(films);
        return films;
    }

    private Set<Genre> getGenresById(Integer filmId) {
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(
                "SELECT g.ID, g.NAME FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID WHERE fg.FILM_ID = ?",
                filmId);
        LinkedHashSet<Genre> filmGenres = new LinkedHashSet<>();
        while (genresRowSet.next()) {
            Genre genre = new Genre(genresRowSet.getInt("id"),
                    genresRowSet.getString("name"));
            filmGenres.add(genre);
        }
        return filmGenres;
    }

    private void updateLikes(Film film) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILM_ID = ?",
                film.getId());
        Set<Integer> newLikes = film.getLikes();
        newLikes.forEach(userId ->
                jdbcTemplate.update("INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)",
                        userId, film.getId()));
    }

    private void updateGenres(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?",
                film.getId());
        Set<Integer> filmGenreIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        filmGenreIds.forEach(filmGenreId ->
                jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                        film.getId(), filmGenreId));
    }

    private void setGenresAndLikesForFilms(List<Film> films) {
        Map<Integer, Film> mapFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));

        setGenresForFilmsByIds(mapFilms);
        setLikesByFilmsIds(mapFilms);
    }

    private void setLikesByFilmsIds(Map<Integer, Film> films) {
        class Row {
            Integer filmId;
            Integer userId;
        }
        StringJoiner parameter = new StringJoiner(",");
        films.keySet().forEach(id -> parameter.add(Long.toString(id)));

        List<Row> result = jdbcTemplate.query("SELECT * FROM LIKES WHERE FILM_ID IN ".concat("(").concat(parameter.toString()).concat(")"),
                (rs, rowNum) -> {
                    Row row = new Row();
                    row.filmId = rs.getInt("film_Id");
                    row.userId = rs.getInt("user_id");
                    return row;
                }
        );

        films.values().forEach(film -> film.setLikes(new HashSet<>()));

        for (Row row : result) {
            Integer userId = row.userId;
            Set<Integer> userLikes = films.get(row.filmId).getLikes();
            userLikes.add(userId);
        }
    }

    private void setGenresForFilmsByIds(Map<Integer, Film> films) {
        class Row {
            Integer filmId;
            Integer genreId;
            String genreName;
        }

        StringJoiner parameter = new StringJoiner(",");
        films.keySet().forEach(id -> parameter.add(Long.toString(id)));

        List<Row> result = jdbcTemplate.query("SELECT fg.FILM_Id as film_id, g.ID as genre_id, g.NAME as genre_name " +
                        "FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID " +
                        "WHERE fg.FILM_ID IN ".concat("(").concat(parameter.toString()).concat(")"),
                (rs, rowNum) -> {
                    Row row = new Row();
                    row.filmId = rs.getInt("film_Id");
                    row.genreId = rs.getInt("genre_id");
                    row.genreName = rs.getString("genre_name");
                    return row;
                }
        );

        films.values().forEach(film -> film.setGenres(new LinkedHashSet<>()));

        for (Row row : result) {
            Integer filmId = row.filmId;
            Set<Genre> genres = films.get(filmId).getGenres();
            genres.add(new Genre(row.genreId, row.genreName));
        }
    }
}