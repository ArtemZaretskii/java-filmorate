package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    int id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes = new HashSet<>();
}
