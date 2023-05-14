package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendDaoTest {
    private final FriendDao friendDao;
    private final UserDbStorage userDbStorage;
    private final UserService userService;
    private User user;

    @BeforeEach
    public void createUserForTests() {
        user = new User(2, "salvador@mail.ru", "chico", "Poco",
                LocalDate.of(1990, 10, 6), new HashSet<>(), new HashMap<>(), new HashSet<>());
    }

    @Test
    void shouldAddToFriends() {
        user = userDbStorage.add(user);
        friendDao.addFriend(1, 2);
        List<User> result = new ArrayList<>(userService.getFriends(1));
        assertThat(userService.getFriends(1)).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).hasFieldOrPropertyWithValue("id", 2);
    }

    @Test
    void shouldAddToFriendsAcceptedFriendship() {
        userDbStorage.add(user);
        friendDao.addFriend(1, 2);
        friendDao.addFriend(2, 1);

        List<User> result1 = new ArrayList<>(userService.getFriends(1));
        List<User> result2 = new ArrayList<>(userService.getFriends(2));

        assertThat(result1).isNotEmpty();
        assertThat(result2).isNotEmpty();

        assertThat(result1.get(0)).hasFieldOrPropertyWithValue("id", 2);
        assertThat(result2.get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void shouldRemoveFromFriends() {
        userDbStorage.add(user);
        friendDao.addFriend(1, 2);
        List<User> result1 = new ArrayList<>(userService.getFriends(1));

        assertThat(result1).isNotEmpty();
        friendDao.deleteFriend(1, 2);
        List<User> result2 = new ArrayList<>(userService.getFriends(1));
        assertThat(result2).isEmpty();
    }

    @Test
    void shouldReturnUserFriends() {
        userDbStorage.add(user);
        friendDao.addFriend(1, 2);
        List<User> result = new ArrayList<>(friendDao.getFriends(1));
        assertThat(result.get(0).getName()).isEqualTo(user.getName());
    }

    @Test
    void shouldReturnMutualFriends() {
        User common = new User(3, "common@mail.ru", "common", "common",
                LocalDate.of(1980, 10, 6), new HashSet<>(), new HashMap<>(), new HashSet<>());
        userDbStorage.add(user);
        userDbStorage.add(common);
        friendDao.addFriend(1, 3);
        friendDao.addFriend(2, 3);
        List<User> result = new ArrayList<>(friendDao.getListOfCommonFriends(1, 2));
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(3);
    }
}