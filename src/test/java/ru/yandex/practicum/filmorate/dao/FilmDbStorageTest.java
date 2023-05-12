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
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        film = new Film("The Rock1", "Starring Nicolas Cage and Sean Connery1",
                LocalDate.of(1995, 6, 7), 137, new Rating(2));
        film1 = new Film("The Rock2", "Starring Nicolas Cage and Sean Connery2",
                LocalDate.of(1994, 6, 7), 135, new Rating(3),
                new HashSet<>(List.of(new Genre(1), new Genre(2))));
    }

    @Test
    void shouldReturnAllFilmsWhenFilmHasNotGenres() {
        Map<Integer, Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(1);
        Film returnedFilm = films.get(1);
        assertThat(returnedFilm.getId()).isEqualTo(1);
        assertThat(returnedFilm.getName()).isEqualTo("The Rock");
        assertThat(returnedFilm.getDescription()).isEqualTo("Starring Nicolas Cage and Sean Connery");
        assertThat(returnedFilm.getReleaseDate()).isEqualTo("1996-06-07");
        assertThat(returnedFilm.getDuration()).isEqualTo(136);
        assertThat(returnedFilm.getRating().getId()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllFilmsWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        Map<Integer, Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(2);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film1.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(returnedFilm.getRating().getId()).isEqualTo(film1.getRating().getId());
        assertThat(returnedFilm.getGenres()).isEqualTo(film1.getGenres());
    }

    @Test
    void shouldAddFilmWhenFilmHasNotGenres() {
        filmDbStorage.add(film);
        Map<Integer, Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(2);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(returnedFilm.getRating().getId()).isEqualTo(film.getRating().getId());
    }

    @Test
    void shouldAddFilmWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        Map<Integer, Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        Film returnedFilm = films.get(2);
        assertThat(returnedFilm.getId()).isEqualTo(2);
        assertThat(returnedFilm.getName()).isEqualTo(film1.getName());
        assertThat(returnedFilm.getDescription()).isEqualTo(film1.getDescription());
        assertThat(returnedFilm.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(returnedFilm.getDuration()).isEqualTo(film1.getDuration());
        assertThat(returnedFilm.getRating().getId()).isEqualTo(film1.getRating().getId());
        assertThat(returnedFilm.getGenres()).isEqualTo(film1.getGenres());
    }

    @Test
    void shouldUpdateFilmWhenFilmHasNotGenres() {
        Film updateFilm = new Film(1, "updateName", "updateDescription", LocalDate.of(2000, 12, 12),
                100, new HashSet<>(), new Rating(4), null);
        filmDbStorage.update(updateFilm);
        assertThat(updateFilm).isEqualTo(filmDbStorage.getFilms().get(1));
    }

    @Test
    void shouldUpdateFilmWhenFilmHasGenres() {
        filmDbStorage.add(film1);
        Film updateFilm = new Film(2, "updateName", "updateDescription", LocalDate.of(2000, 12, 12),
                100,new HashSet<>(),  new Rating(4), new HashSet<>(List.of(new Genre(3), new Genre(4))));
        filmDbStorage.update(updateFilm);
        assertThat(updateFilm).isEqualTo(filmDbStorage.getFilms().get(2));
    }

    @Test
    void shouldRemoveFilm() {
        assertThat(filmDbStorage.getFilms().get(1).getId()).isEqualTo(1);
        assertThat(filmDbStorage.getFilms()).hasSize(1);
        filmDbStorage.deleteFilm(1);
        assertThat(filmDbStorage.getFilms()).isEmpty();
    }

    @Test
    void shouldReturnFilmById() {
        assertThat(filmDbStorage.getFilmById(1)).isEqualTo(new Film(1, "The Rock",
                "Starring Nicolas Cage and Sean Connery", LocalDate.of(1996, 6, 7),
                136, new HashSet<>(), new Rating(1), null));
    }
}