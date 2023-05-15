package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreDao genreDao;

    public Genre getGenreById(Integer id) {
        log.info("Запрос жанра с id='{}'", id);
        return genreDao.findById(id);
    }

    public List<Genre> getAllGenres() {
        log.info("Запрос всех жанров");
        return genreDao.findAll();
    }
}