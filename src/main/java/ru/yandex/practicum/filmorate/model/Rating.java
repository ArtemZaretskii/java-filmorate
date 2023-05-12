package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Rating {
    private int id;
    private String name;

    @JsonCreator
    public Rating(@JsonProperty("id") int id) {
        this.id = id;
        this.name = TypeOfRating.values()[id - 1].getTitle();
    }
}