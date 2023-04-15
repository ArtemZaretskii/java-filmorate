package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTests {
    private FilmController filmController;

    @BeforeEach
    void startTests() {
        filmController = new FilmController();

        Film film = new Film(
                "Побег из Шоушенка",
                "Бухгалтер Энди Дюфрейн обвинён в убийстве своей жены и её любовника. В тюрьме под названием Шоушенк, " +
                        "он сталкивается с беззаконием. Каждый, в этих стенах, становится их рабом до конца жизни.", // 190 символов
                LocalDate.of(1994, 9, 10),
                Duration.ofMinutes(142));
        filmController.add(film);
    }

    @Test
    void addNewFilm() {
        Film film = new Film(
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilm");
    }

    @Test
    void addNewFilmWithIdIsException() {
        Film film = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewUserWithIdIsException");
    }

    @Test
    void addNewFilmWithoutNameIsException() {
        Film film = new Film(
                "",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithoutNameIsException");
    }

    @Test
    void addNewFilmWithDescriptionLengthOf200Characters() {
        Film film = new Film(
                "Зеленая миля",
                "******************************************************************" +
                        "*******************************************************************" +
                        "*******************************************************************", // 200 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithDescriptionLengthOf200Characters");
    }

    @Test
    void addNewFilmWithDescriptionLengthOf201CharactersIsException() {
        Film film = new Film(
                "Зеленая миля",
                "******************************************************************" +
                        "*******************************************************************" +
                        "*******************************************************************" +
                        "*", // 201 символ
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithDescriptionLengthOf201CharactersIsException");
    }

    @Test
    void addNewFilmWithoutDescription() {
        Film film = new Film(
                "Зеленая миля",
                "",
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithoutDescription");
    }

    @Test
    void addNewFilmWithReleaseDate_28_12_1895() {
        Film film = new Film(
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1895, 12, 28),
                Duration.ofMinutes(189));
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithReleaseDate_28_12_1895");
    }

    @Test
    void addNewFilmWithReleaseDateOf27_12_1895IsException() {
        Film film = new Film(
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1895, 12, 27),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithReleaseDateOf27_12_1895IsException");
    }

    @Test
    void addNewFilmWithNegativeDurationIsException() {
        Film film = new Film(
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(-1));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithNegativeDurationIsException");
    }

    @Test
    void addNewFilmWithZeroDurationIsException() {
        Film film = new Film(
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(0));
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithZeroDurationIsException");
    }

    @Test
    void updateFilm() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.update(updateFilm);

        assertTrue(filmController.getFilms().contains(updateFilm), "Ошибка теста addNewFilmAndUpdateFilm");
        assertEquals("Зеленая миля", filmController.getFilms().get(0).getName());
        assertEquals("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", filmController.getFilms().get(0).getDescription());
        assertEquals(LocalDate.of(1999, 12, 6), filmController.getFilms().get(0).getReleaseDate());
        assertEquals(Duration.ofMinutes(189), filmController.getFilms().get(0).getDuration());
    }

    @Test
    void updateFilmNotFoundIdIsException() {
        Film updateFilm = new Film(6,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithoutNameIsException");
    }

    @Test
    void updateFilmWithoutNameIsException() {
        Film updateFilm = new Film(
                1,
                "",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithoutNameIsException");
    }

    @Test
    void updateFilmWithDescriptionLengthOf200Characters() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "******************************************************************" +
                        "*******************************************************************" +
                        "*******************************************************************", // 200 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithDescriptionLengthOf200Characters");
    }

    @Test
    void updateFilmWithDescriptionLengthOf201CharactersIsException() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "******************************************************************" +
                        "*******************************************************************" +
                        "*******************************************************************" +
                        "*", // 201 символ
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithDescriptionLengthOf201CharactersIsException");
    }

    @Test
    void updateFilmWithoutDescription() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "",
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(189));
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithoutDescription");
    }

    @Test
    void updateFilmWithReleaseDate_28_12_1895() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1895, 12, 28),
                Duration.ofMinutes(189));
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithReleaseDate_28_12_1895");
    }

    @Test
    void updateFilmWithReleaseDateOf27_12_1895IsException() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1895, 12, 27),
                Duration.ofMinutes(189));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithReleaseDateOf27_12_1895IsException");
    }

    @Test
    void updateFilmWithNegativeDurationIsException() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(-1));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithNegativeDurationIsException");
    }

    @Test
    void updateFilmWithWithZeroDurationIsException() {
        Film updateFilm = new Film(
                1,
                "Зеленая миля",
                "Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                        "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", // 140 символов
                LocalDate.of(1999, 12, 6),
                Duration.ofMinutes(0));
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithWithZeroDurationIsException");
    }
}
