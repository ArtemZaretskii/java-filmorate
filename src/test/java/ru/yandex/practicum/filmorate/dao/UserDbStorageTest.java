package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    public void createUserForTests() {
        user = User.builder()
                .email("salvador@mail.ru")
                .login("chico")
                .name("Poco")
                .birthday(LocalDate.of(1990, 10, 6))
                .build();
    }

    @Test
    void shouldReturnUserWhenDbHasUser() {
        List<User> users = userDbStorage.getUsers();
        assertThat(users).hasSize(1);
        User testUser = users.get(0);
        assertThat(testUser.getEmail()).isEqualTo("mail");
        assertThat(testUser.getLogin()).isEqualTo("login");
        assertThat(testUser.getName()).isEqualTo("name");
        assertThat(testUser.getBirthday()).isEqualTo("1990-03-08");
    }

    @Test
    void shouldAddUser() {
        userDbStorage.add(user);
        List<User> users = userDbStorage.getUsers();
        User testUser = users.get(1);
        assertThat(testUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(testUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(testUser.getName()).isEqualTo(user.getName());
        assertThat(testUser.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    void shouldUpdateUser() {
        User updateUser = User.builder()
                .id(1)
                .email("updateEmail")
                .login("updateLogin")
                .name("updateName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userDbStorage.update(updateUser);
        User returnedUser = userDbStorage.getUsers().get(0);
        assertThat(returnedUser).isEqualTo(updateUser);
    }

    @Test
    void shouldRemoveUser() {
        assertThat(userDbStorage.getUsers().get(0).getId()).isEqualTo(1);
        assertThat(userDbStorage.getUsers()).hasSize(1);
        userDbStorage.deleteUser(1);
        assertThat(userDbStorage.getUsers()).isEmpty();
    }

    @Test
    void shouldReturnUserById() {
        User expectedUser = User.builder()
                .id(1)
                .name("name")
                .email("mail")
                .login("login")
                .birthday(LocalDate.of(1990, 3, 8))
                .build();

        assertThat(userDbStorage.getUserById(1)).isEqualTo(expectedUser);
    }
}