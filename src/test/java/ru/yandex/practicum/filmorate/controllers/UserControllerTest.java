package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;
    private User user;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private User user7;
    private User user8;

    @BeforeEach
    public void start() {
        UserStorage userStorage = new InMemoryUserStorage();
        controller = new UserController(new UserService(userStorage));
        createUsersForTests();
    }

    private void createUsersForTests() {
        user = User.builder()
                .id(1)
                .email("salvador@mail.ru")
                .login("chico")
                .name("Poco")
                .birthday(LocalDate.of(1990, 10, 6))
                .build();
        user1 = User.builder()
                .id(1)
                .email("")
                .login("chico1")
                .name("Poco1")
                .birthday(LocalDate.of(1991, 10, 6))
                .build();
        user2 = User.builder()
                .id(1)
                .email("salvador.ru")
                .login("chico2")
                .name("Poco2")
                .birthday(LocalDate.of(1992, 10, 6))
                .build();
        user3 = User.builder()
                .id(1)
                .email("salvador@t.ru")
                .login("")
                .name("Poco3")
                .birthday(LocalDate.of(1993, 10, 6))
                .build();
        user4 = User.builder()
                .id(1)
                .email("salvador@t.ru")
                .login("chico and son")
                .name("Poco4")
                .birthday(LocalDate.of(1994, 10, 6))
                .build();
        user5 = User.builder()
                .id(1)
                .email("salvador@t.ru5")
                .login("chico5")
                .name("")
                .birthday(LocalDate.of(1995, 10, 6))
                .build();
        user6 = User.builder()
                .id(1)
                .email("salvador@t.ru6")
                .login("chico6")
                .name("Poco6")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        user7 = User.builder()
                .id(1)
                .email("salvador@t.ru7")
                .login("chico7")
                .name("Poco7")
                .birthday(LocalDate.now())
                .build();
        user8 = User.builder()
                .id(1)
                .email("salvador@t.ru8")
                .login("chico8")
                .name("Poco8")
                .birthday(LocalDate.now().plusDays(1))
                .build();
    }

    @Test
    void shouldReturnAllUsers() {
        assertEquals(0, controller.getUsers().size(), "Хранилище должно быть пустым.");
        controller.add(user);
        Collection<User> users = controller.getUsers();
        assertEquals(1, users.size(), "Хранилище не должно быть пустым.");
        assertTrue(users.contains(user), "Пользователь не добавлен.");
    }

    @Test
    void shouldAddUserWhenDataIsValid() {
        controller.add(user);
        assertTrue(controller.getUsers().contains(user), "Пользователь не добавлен в хранилище.");
    }

    @Test
    void shouldNotAddUserWhenEmailIsEmpty() {
        assertThrows(ValidationException.class, () -> controller.add(user1), "Email не пустой.");
        assertFalse(controller.getUsers().contains(user1), "Фильм добавлен в хранилище.");
    }

    @Test
    void shouldNotAddUserWhenEmailIsNotContainsSymbol() {
        assertThrows(ValidationException.class, () -> controller.add(user2), "Email содержит символ @.");
        assertFalse(controller.getUsers().contains(user2), "Пользователь добавлен в хранилище.");
    }

    @Test
    void shouldNotAddUserWhenLoginIsEmpty() {
        assertThrows(ValidationException.class, () -> controller.add(user3), "Логин не пустой.");
        assertFalse(controller.getUsers().contains(user3), "Пользователь добавлен в хранилище.");
    }

    @Test
    void shouldNotAddUserWhenLoginContainsSpaces() {
        assertThrows(ValidationException.class, () -> controller.add(user4), "Логин не содержит пробелы.");
        assertFalse(controller.getUsers().contains(user4), "Пользователь добавлен в хранилище.");
    }

    @Test
    void shouldAddUserWhenNameIsEmpty() {
        controller.add(user5);
        assertTrue(controller.getUsers().contains(user5), "Пользователь не добавлен в хранилище.");
        assertEquals(user5.getLogin(), user5.getName(), "Логин и имя различны.");
    }

    @Test
    void shouldAddUserWhenBirthdayIsBeforeNow() {
        controller.add(user6);
        assertTrue(controller.getUsers().contains(user6), "Пользователь не добавлен в хранилище.");
    }

    @Test
    void shouldAddUserWhenBirthdayIsEqualsNow() {
        controller.add(user7);
        assertTrue(controller.getUsers().contains(user7), "Пользователь не добавлен в хранилище.");
    }

    @Test
    void shouldNotAddUserWhenBirthdayIsAfterNow() {
        assertThrows(ValidationException.class, () -> controller.add(user8), "Дата рождения не в будущем.");
        assertFalse(controller.getUsers().contains(user8), "Пользователь добавлен в хранилище.");
    }

    @Test
    void shouldUpdateUserWhenDataIsValid() {
        controller.add(user);
        assertEquals(1, controller.getUsers().size(), "Хранилище не должно быть пустым.");
        assertTrue(controller.getUsers().contains(user), "Фильм не добавлен в хранилище.");
        User result = controller.update(user6);
        assertEquals(1, controller.getUsers().size(), "Хранилище не должно быть пустым.");
        assertEquals(result.getEmail(), user6.getEmail(), "Адреса email не совпадают.");
        assertEquals(result.getLogin(), user6.getLogin(), "Логины не совпадают.");
        assertEquals(result.getName(), user6.getName(), "Имена не совпадают.");
        assertEquals(result.getBirthday(), user6.getBirthday(), "Даты рождения не совпадают.");
    }

    @Test
    void shouldNotUpdateUserWhenEmailIsEmpty() {
        controller.add(user);
        assertThrows(ValidationException.class, () -> controller.update(user1), "Email не пустой.");
        assertNotEquals(user.getEmail(), user1.getEmail(), "Адреса email совпадают.");
        assertNotEquals(user.getLogin(), user1.getLogin(), "Логины совпадают.");
        assertNotEquals(user.getName(), user1.getName(), "Имена совпадают.");
        assertNotEquals(user.getBirthday(), user1.getBirthday(), "Даты рождения совпадают.");
    }

    @Test
    void shouldNotUpdateUserWhenEmailIsNotContainsSymbol() {
        controller.add(user);
        assertThrows(ValidationException.class, () -> controller.update(user2), "Email содержит символ @.");
        assertNotEquals(user.getEmail(), user2.getEmail(), "Адреса email совпадают.");
        assertNotEquals(user.getLogin(), user2.getLogin(), "Логины совпадают.");
        assertNotEquals(user.getName(), user2.getName(), "Имена совпадают.");
        assertNotEquals(user.getBirthday(), user2.getBirthday(), "Даты рождения совпадают.");
    }

    @Test
    void shouldNotUpdateUserWhenLoginIsEmpty() {
        controller.add(user);
        assertThrows(ValidationException.class, () -> controller.update(user3), "Логин не пустой.");
        assertNotEquals(user.getEmail(), user3.getEmail(), "Адреса email совпадают.");
        assertNotEquals(user.getLogin(), user3.getLogin(), "Логины совпадают.");
        assertNotEquals(user.getName(), user3.getName(), "Имена совпадают.");
        assertNotEquals(user.getBirthday(), user3.getBirthday(), "Даты рождения совпадают.");
    }

    @Test
    void shouldNotUpdateUserWhenLoginContainsSpace() {
        controller.add(user);
        assertThrows(ValidationException.class, () -> controller.update(user4), "Логин не содержит пробелы.");
        assertNotEquals(user.getEmail(), user4.getEmail(), "Адреса email совпадают.");
        assertNotEquals(user.getLogin(), user4.getLogin(), "Логины совпадают.");
        assertNotEquals(user.getName(), user4.getName(), "Имена совпадают.");


        assertNotEquals(user.getBirthday(), user4.getBirthday(), "Даты рождения совпадают.");
    }

    @Test
    void shouldUpdateUserWhenNameIsEmpty() {
        controller.add(user);
        assertTrue(controller.getUsers().contains(user), "Пользователь не добавлен в хранилище.");
        User result = controller.update(user5);
        assertEquals(result.getEmail(), user5.getEmail(), "Адреса email не совпадают.");
        assertEquals(result.getLogin(), user5.getLogin(), "Логины не совпадают.");
        assertEquals(result.getName(), user5.getName(), "Имена не совпадают.");
        assertEquals(result.getBirthday(), user5.getBirthday(), "Даты рождения не совпадают.");
    }

    @Test
    void shouldUpdateUserWhenBirthdayIsBeforeNow() {
        controller.add(user);
        assertTrue(controller.getUsers().contains(user), "Пользователь не добавлен в хранилище.");
        User result = controller.update(user6);
        assertEquals(result.getEmail(), user6.getEmail(), "Адреса email не совпадают.");
        assertEquals(result.getLogin(), user6.getLogin(), "Логины не совпадают.");
        assertEquals(result.getName(), user6.getName(), "Имена не совпадают.");
        assertEquals(result.getBirthday(), user6.getBirthday(), "Даты рождения не совпадают.");
    }

    @Test
    void shouldUpdateUserWhenBirthdayIsEqualsNow() {
        controller.add(user);
        assertTrue(controller.getUsers().contains(user), "Пользователь не добавлен в хранилище.");
        User result = controller.update(user7);
        assertEquals(result.getEmail(), user7.getEmail(), "Адреса email не совпадают.");
        assertEquals(result.getLogin(), user7.getLogin(), "Логины не совпадают.");
        assertEquals(result.getName(), user7.getName(), "Имена не совпадают.");
        assertEquals(result.getBirthday(), user7.getBirthday(), "Даты рождения не совпадают.");
    }

    @Test
    void shouldNotUpdateUserWhenBirthdayIsAfterNow() {
        controller.add(user);
        assertThrows(ValidationException.class, () -> controller.update(user8), "Дата рождения не в будущем.");
        assertNotEquals(user.getEmail(), user8.getEmail(), "Адреса email совпадают.");
        assertNotEquals(user.getLogin(), user8.getLogin(), "Логины совпадают.");
        assertNotEquals(user.getName(), user8.getName(), "Имена совпадают.");

        assertNotEquals(user.getBirthday(), user8.getBirthday(), "Даты рождения совпадают.");
    }
}