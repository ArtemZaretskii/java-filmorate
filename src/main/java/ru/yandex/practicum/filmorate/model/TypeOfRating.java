package ru.yandex.practicum.filmorate.model;

public enum TypeOfRating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");
    private final String title;

    TypeOfRating(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}