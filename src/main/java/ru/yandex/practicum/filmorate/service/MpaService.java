package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa getMpaById(Long id) {
        log.info("Запрос на получение рейтинга mpa с id='{}'", id);
        return mpaDao.findById(id);
    }

    public List<Mpa> getAllMpa() {
        log.info("Запрос на получение всех рейтингов mpa");
        return mpaDao.findAll();
    }
}