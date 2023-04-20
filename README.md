# java-filmorate
Template repository for Filmorate project.

Приветствую Артем! Спасибо!

Насчет комментариев в Pull Requests знаю, но пока не видел.

Извиняюсь за бардак.
Тесты привел в нормальный вид, остальное вернул как было, а то 
по советам, как обойти ошибки постмана и поискам в инете уже примерно в таком виде код был:
 
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor

    public class User {
    int id;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Указан некорректный Email")
    String email;
    @NotBlank(message = "Login не может быть пустым")
    String login;
    String name;
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}

Вот только пока не понял, как обложить это тестами.