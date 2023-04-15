package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {
    private UserController userController;

    @BeforeEach
    void start() {
        userController = new UserController();
        User user = new User(
                "petrov@ya.ru",
                "Petrov",
                "Petr Petrov",
                LocalDate.of(2001, 9, 9));
        userController.add(user);
    }

    @Test
    void addNewUser() {
        User user = new User(
                "ivanov@ya.ru",
                "Ivan",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        userController.add(user);
        assertTrue(userController.getUsers().contains(user),
                "Ошибка теста addNewUser");
    }

    @Test
    void addNewUserWithIdIsException() {
        User user = new User(
                5,
                "ivanov@ya.ru",
                "Ivan",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserWithIdIsException");
    }

    @Test
    void addNewUserWithoutEmailIsException() {
        User user = new User(
                "",
                "Ivan",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserWithoutEmailIsException");
    }

    @Test
    void addNewUserEmailContainsOnlySpacesIsException() {
        User user = new User(
                "     ",
                "Ivan",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserEmailContainsOnlySpacesIsException");
    }

    @Test
    void addNewUserContainsIncorrectEmailIsException() {
        User user = new User(
                "ivanov.mail.ru",
                "Ivan",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserContainsIncorrectEmailIsException");
    }

    @Test
    void addNewUserWithoutLoginIsException() {
        User user = new User(
                "ivanov@ya.ru",
                "",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserWithoutLoginIsException");
    }

    @Test
    void addNewUserLoginContainsSpacesIsException() {
        User user = new User(
                "ivanov@ya.ru",
                "I v a n",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserLoginContainsSpacesIsException");
    }

    @Test
    void addNewUserLoginContainsOnlySpacesIsException() {
        User user = new User(
                "ivanov@ya.ru",
                "     ",
                "Ivan Ivanov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserLoginContainsOnlySpacesIsException");
    }

    @Test
    void addNewUserWithoutName() {
        User user = new User(
                "ivanov@ya.ru",
                "Ivan1",
                "",
                LocalDate.of(1999, 1, 1));
        userController.add(user);
        assertEquals("Ivan1", userController.getUsers().get(1).getName(),
                "Ошибка теста addNewUserWithoutName");
    }

    @Test
    void addNewUserWithDateOfBirthInFutureIsException() {
        User user = new User(
                "ivanov@ya.ru",
                "Ivan1",
                "",
                LocalDate.of(2024, 1, 1));
        assertThrows(ValidationException.class, () -> userController.add(user),
                "Ошибка теста addNewUserWithDateOfBirthInFutureIsException");
    }

    @Test
    void updateUser() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        userController.update(user);
        assertEquals("petrov@gmail.com", userController.getUsers().get(0).getEmail());
        assertEquals("IvanPetrov", userController.getUsers().get(0).getLogin());
        assertEquals("Ivan Petrov", userController.getUsers().get(0).getName());
    }

    @Test
    void updateUserIncorrectIdIsException() {
        User user = new User(
                5,
                "petrov@gmail.com",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserIncorrectIdIsException");
    }

    @Test
    void updateUserWithoutEmailIsException() {
        User user = new User(
                1,
                "",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserWithoutEmailIsException");
    }

    @Test
    void updateUserEmailContainsOnlySpacesIsException() {
        User user = new User(
                1,
                "            ",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserEmailContainsOnlySpacesIsException");
    }

    @Test
    void updateUserContainsIncorrectEmailIsException() {
        User user = new User(
                1,
                "petrov.mail.ru",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserContainsIncorrectEmailIsException");
    }

    @Test
    void updateUserWithoutLoginIsException() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserWithoutLoginIsException");
    }

    @Test
    void updateUserLoginContainsSpacesIsException() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "I v a n P e t r o v",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserLoginContainsSpacesIsException");
    }

    @Test
    void updateUserLoginContainsOnlySpacesIsException() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "               ",
                "Ivan Petrov",
                LocalDate.of(1999, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserLoginContainsOnlySpacesIsException");
    }

    @Test
    void updateUserWithoutName() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "IvanPetrov",
                "",
                LocalDate.of(1999, 1, 1));
        userController.update(user);
        assertEquals("IvanPetrov", userController.getUsers().get(0).getName(),
                "Ошибка теста updateUserWithoutName");
    }

    @Test
    void updateUserNameContainsOnlySpaces() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "IvanPetrov",
                "              ",
                LocalDate.of(1999, 1, 1));
        userController.update(user);
        assertEquals("IvanPetrov", userController.getUsers().get(0).getName(),
                "Ошибка теста updateUserNameContainsOnlySpaces");
    }

    @Test
    void updateUserWithDateOfBirthInFutureIsException() {
        User user = new User(
                1,
                "petrov@gmail.com",
                "IvanPetrov",
                "Ivan Petrov",
                LocalDate.of(2024, 1, 1));
        assertThrows(ValidationException.class, () -> userController.update(user),
                "Ошибка теста updateUserWithDateOfBirthInFutureIsException");
    }

    @Test
    void sout() {
        System.out.println(userController.getUsers());
    }
}
