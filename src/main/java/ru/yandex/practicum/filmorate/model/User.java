package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    Integer id;

    @NotBlank
    String email;

    @NotBlank
    String login;

    String name;

    @Past
    LocalDate birthday;

    Set<Integer> friends;

    Map<Integer, Boolean> friendStatus;

    Set<Integer> likedFilms;
}

