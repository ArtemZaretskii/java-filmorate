package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.TypeOfRating;

import java.util.Collection;

@Service
public class RatingService {
    private RatingDao ratingDao;

    @Autowired
    public RatingService(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public Rating getRatingById(Integer id) {
        if (id > TypeOfRating.values().length - 1 || id < 1) {
            throw new ObjectNotFoundException(String.format("id %d", id));
        }
        return RatingDao.getRatingById(id);
    }

    public Collection<Rating> getRatings() {
        return RatingDao.getRatings();
    }
}