package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikesDaoTest {
    private final LikesDao likesDao;
    private final FilmDbStorage filmDbStorage;

    @Test
    void shouldAddLike() {
        assertThat(filmDbStorage.getFilms().get(1).getLikes()).isEmpty();
        likesDao.addLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(1).getLikes()).contains(1);
    }

    @Test
    void shouldRemoveLike() {
        assertThat(filmDbStorage.getFilms().get(1).getLikes()).isEmpty();
        likesDao.addLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(1).getLikes()).contains(1);
        likesDao.deleteLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(1).getLikes()).isEmpty();
    }

    @Test
    void shouldReturnPopularFilms() {
        Film film = new Film(2,"The Rock1", "Starring Nicolas Cage and Sean Connery1",
                LocalDate.of(1995, 6, 7), 137, new Rating(2), new HashSet<>());
        filmDbStorage.add(film);
        likesDao.addLike(2, 1);
        assertThat(likesDao.getTop10Films(2)).isEqualTo(List.of(new Film(2,"The Rock1",
                "Starring Nicolas Cage and Sean Connery1", LocalDate.of(1995, 6, 7),
                137, new HashSet<>(List.of(1)), new Rating(2), new HashSet<>()), new Film(1,"The Rock",
                "Starring Nicolas Cage and Sean Connery", LocalDate.of(1996, 6, 7),
                136, new Rating(1), new HashSet<>())));
    }
}