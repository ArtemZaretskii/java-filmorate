package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTests {
    private FilmController filmController;

    @BeforeEach
    void startTests() {
        filmController = new FilmController();

        Film film = new Film();
        film.setName("Побег из Шоушенка");
        film.setDescription("Бухгалтер Энди Дюфрейн обвинён в убийстве своей жены и её любовника." +
                " В тюрьме под названием Шоушенк, он сталкивается с беззаконием. Каждый, в этих стенах, " +
                "становится их рабом до конца жизни."); // 190 символов
        film.setReleaseDate(LocalDate.of(1994, 9, 10));
        film.setDuration(142);
        filmController.add(film);
    }

    @Test
    void addNewFilm() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilm");
    }

    @Test
    void addNewFilmWithoutNameIsException() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithoutNameIsException");
    }

    @Test
    void addNewFilmWithDescriptionLengthOf200Characters() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("******************************************************************" +
                "*******************************************************************" +
                "*******************************************************************"); // 200 символов
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithDescriptionLengthOf200Characters");
    }

    @Test
    void addNewFilmWithDescriptionLengthOf201CharactersIsException() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("******************************************************************" +
                "*******************************************************************" +
                "*******************************************************************" +
                "*"); // 201 символ
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithDescriptionLengthOf201CharactersIsException");
    }

    @Test
    void addNewFilmWithoutDescription() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription(""); // 0 символов
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(189);
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithoutDescription");
    }

    @Test
    void addNewFilmWithReleaseDate_28_12_1895() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(189);
        filmController.add(film);
        assertTrue(filmController.getFilms().contains(film),
                "Ошибка теста addNewFilmWithReleaseDate_28_12_1895");
    }

    @Test
    void addNewFilmWithReleaseDateOf27_12_1895IsException() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithReleaseDateOf27_12_1895IsException");
    }

    @Test
    void addNewFilmWithNegativeDurationIsException() {
        Film film = new Film();
        film.setName("Зеленая миля");
        film.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        film.setReleaseDate(LocalDate.of(1999, 12, 6));
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.add(film),
                "Ошибка теста addNewFilmWithNegativeDurationIsException");
    }

    @Test
    void updateFilm() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(189);
        filmController.update(updateFilm);

        assertTrue(filmController.getFilms().contains(updateFilm), "Ошибка теста addNewFilmAndUpdateFilm");
        assertEquals("Зеленая миля", filmController.getFilms().get(0).getName());
        assertEquals("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни.", filmController.getFilms().get(0).getDescription());
        assertEquals(LocalDate.of(1999, 12, 6), filmController.getFilms().get(0).getReleaseDate());
        assertEquals(189, filmController.getFilms().get(0).getDuration());
    }

    @Test
    void updateFilmWithoutNameIsException() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithoutNameIsException");
    }

    @Test
    void updateFilmWithDescriptionLengthOf200Characters() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("******************************************************************" +
                "*******************************************************************" +
                "*******************************************************************"); // 200 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(189);
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithDescriptionLengthOf200Characters");
    }

    @Test
    void updateFilmWithDescriptionLengthOf201CharactersIsException() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("******************************************************************" +
                "*******************************************************************" +
                "*******************************************************************" +
                "*"); // 201 символ
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithDescriptionLengthOf201CharactersIsException");
    }

    @Test
    void updateFilmWithoutDescription() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription(""); // 0 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(189);
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithoutDescription");
    }

    @Test
    void updateFilmWithReleaseDate_28_12_1895() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        updateFilm.setDuration(189);
        filmController.update(updateFilm);
        assertTrue(filmController.getFilms().contains(updateFilm),
                "Ошибка теста updateFilmWithReleaseDate_28_12_1895");
    }

    @Test
    void updateFilmWithReleaseDateOf27_12_1895IsException() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        updateFilm.setDuration(189);
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithReleaseDateOf27_12_1895IsException");
    }

    @Test
    void updateFilmWithNegativeDurationIsException() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithNegativeDurationIsException");
    }

    @Test
    void updateFilmWithWithZeroDurationIsException() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Зеленая миля");
        updateFilm.setDescription("Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора», " +
                "каждый из узников которого однажды проходит «зеленую милю» к месту казни."); // 140 символов
        updateFilm.setReleaseDate(LocalDate.of(1999, 12, 6));
        updateFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.update(updateFilm),
                "Ошибка теста updateFilmWithWithZeroDurationIsException");
    }
}
