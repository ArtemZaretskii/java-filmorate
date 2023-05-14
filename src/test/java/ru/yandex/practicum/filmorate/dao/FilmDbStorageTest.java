package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private Film film;
    private Film film1;

    @BeforeEach
    public void createFilmsForTests() {
        film = Film.builder()
                .name("The Rock1")
                .description("Starring Nicolas Cage and Sean Connery1")
                .mpa(new Mpa(2, "PG"))
                .releaseDate(LocalDate.of(1995, 6, 7))
                .build();

        film1 = Film.builder()
                .name("The Rock2")
                .description("Starring Nicolas Cage and Sean Connery2")
                .mpa(new Mpa(3, "PG-13"))
                .genres(new HashSet<>(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"))))
                .releaseDate(LocalDate.of(1995, 6, 7))
                .build();
    }

    @Test
    void shouldReturnAllFilmsWhenFilmHasNotGenres() {
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(1);
        Film returnedFilm = films.get(0);
        assertThat(returnedFilm.getId()).isEqualTo(1);
        assertThat(returnedFilm.getName()).isEqualTo("The Rock");
        assertThat(returnedFilm.getDescription()).isEqualTo("Starring Nicolas Cage and Sean Connery");
        assertThat(returnedFilm.getReleaseDate()).isEqualTo("1996-06-07");
        assertThat(returnedFilm.getDuration()).isEqualTo(136);
        assertThat(returnedFilm.getMpa().getId()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllFilmsWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(1);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film1.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(returnedFilm.getMpa().getId()).isEqualTo(film1.getMpa().getId());
        assertThat(returnedFilm.getGenres().size()).isEqualTo(film1.getGenres().size());
    }

    @Test
    void shouldAddFilmWhenFilmHasNotGenres() {
        filmDbStorage.add(film);
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(1);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(returnedFilm.getMpa().getId()).isEqualTo(film.getMpa().getId());
    }

    @Test
    void shouldAddFilmWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(1);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film1.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(returnedFilm.getMpa().getId()).isEqualTo(film1.getMpa().getId());
    }

    @Test
    void shouldUpdateFilmWhenFilmHasNotGenres() {
        Film updateFilm = Film.builder()
                .id(1)
                .name("updateName")
                .description("updateDescription")
                .mpa(new Mpa(2, "PG"))
                .releaseDate(LocalDate.of(2000, 12, 12))
                .mpa(new Mpa(4, "R"))
                .build();
        filmDbStorage.update(updateFilm);
        assertThat(updateFilm.getName()).isEqualTo(filmDbStorage.getFilms().get(0).getName());
    }

    @Test
    void shouldUpdateFilmWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        Film updateFilm = Film.builder()
                .id(2)
                .name("updateName")
                .description("updateDescription")
                .mpa(new Mpa(2, "PG"))
                .releaseDate(LocalDate.of(2000, 12, 12))
                .genres(new HashSet<>(List.of(new Genre(3, "Мультфильм"), new Genre(4, "Триллер"))))
                .mpa(new Mpa(4, "R"))
                .build();

        Film update = filmDbStorage.update(updateFilm);

        Film result = filmDbStorage.getFilmById(update.getId());
        assertThat(updateFilm.getGenres().size()).isEqualTo(result.getGenres().size());
    }

    @Test
    void shouldRemoveFilm() {
        assertThat(filmDbStorage.getFilms().get(0).getId()).isEqualTo(1);
        assertThat(filmDbStorage.getFilms()).hasSize(1);
        filmDbStorage.deleteFilm(1);
        assertThat(filmDbStorage.getFilms()).isEmpty();
    }

    @Test
    void shouldReturnFilmById() {
        Film result = filmDbStorage.getFilmById(1);
        assertThat(result.getName()).isEqualTo("The Rock");
        assertThat(result.getDescription()).isEqualTo("Starring Nicolas Cage and Sean Connery");
    }

    @Test
    void shouldReturnPopularFilms() {
        filmDbStorage.add(film);
        filmDbStorage.addLike(2, 1);

        List<Film> result = new ArrayList<>(filmDbStorage.getPopularFilms(10));
        assertAll(
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0))
                        .hasFieldOrPropertyWithValue("name", film.getName()),
                () -> assertThat(result.get(0))
                        .hasFieldOrPropertyWithValue("mpa.id", film.getMpa().getId()),
                () -> assertThat(result.get(0))
                        .hasFieldOrPropertyWithValue("description", film.getDescription())
        );

    }
}