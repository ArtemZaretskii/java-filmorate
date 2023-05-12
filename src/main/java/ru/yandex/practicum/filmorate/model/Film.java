package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Film {

    int id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes;
    Rating rating;
    Set<Genre> genres;

    public Film(int id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration,
                Rating rating) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
    }

    public Film(String name,
                String description,
                LocalDate releaseDate,
                int duration,
                Rating rating) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
    }

    public Film(String name,
                String description,
                LocalDate releaseDate,
                int duration,
                Rating rating,
                Set<Genre> genres) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
        this.genres = new HashSet<>();
    }

    public Film(int id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration,
                Rating rating,
                Set<Genre> genres) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
        this.genres = new HashSet<>();
    }

    public Film(int id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration,
                Set<Integer> likes,
                Rating rating,
                Set<Genre> genres) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.rating = rating;
        this.genres = new HashSet<>();
    }
}
