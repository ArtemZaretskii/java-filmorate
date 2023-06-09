package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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
        assertThat(filmDbStorage.getFilms().get(0).getLikes()).isEmpty();
        likesDao.addLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(0).getLikes()).contains(1);
    }

    @Test
    void shouldRemoveLike() {
        assertThat(filmDbStorage.getFilms().get(0).getLikes()).isEmpty();
        likesDao.addLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(0).getLikes()).contains(1);
        likesDao.deleteLike(1, 1);
        assertThat(filmDbStorage.getFilms().get(0).getLikes()).isEmpty();
    }


}