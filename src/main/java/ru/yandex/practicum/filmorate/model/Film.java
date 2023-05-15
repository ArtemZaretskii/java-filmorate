package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    int id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes;
    Mpa mpa;
    Set<Genre> genres;
    Integer rate;
}
