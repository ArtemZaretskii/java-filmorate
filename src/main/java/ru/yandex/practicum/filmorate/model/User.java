package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    int id;
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

    public User(int id,
                String email,
                String login,
                String name,
                LocalDate birthday) {

        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String email,
                String login,
                String name,
                LocalDate birthday) {

        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}

