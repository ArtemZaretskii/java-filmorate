package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    @NonNull
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}