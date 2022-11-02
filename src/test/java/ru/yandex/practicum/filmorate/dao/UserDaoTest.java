package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDaoTest {

    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @Test
    void createUser() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);

        assertEquals("Leo99", testUser.getLogin());
        assertEquals(1, testUser.getId());
        assertEquals(1, userDao.findAllUsers().size());
    }

    @Test
    void updateUser() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);
        User forUpdate = new User(1,"leo@mail.ru", "IT", "Leo",
                LocalDate.of(1992, 6, 27));

        userDao.updateUser(forUpdate);
        assertEquals("IT", forUpdate.getLogin());
    }

    @Test
    void deleteUser() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);

        assertEquals(1, userDao.findAllUsers().size());
        userDao.deleteUser(1);
        assertEquals(0, userDao.findAllUsers().size());
    }

    @Test
    void findAllUsers() {

        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);

        User testUser2 = new User("nas@mail.ru", "Nas66", "Nas",
                LocalDate.of(1994, 6, 16));
        userDao.createUser(testUser2);

        assertEquals(2, userDao.findAllUsers().size());
    }

    @Test
    void getUserById() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));

        userDao.createUser(testUser);

        Optional<User> userOptional = Optional.ofNullable(userDao.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getFriends() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);
        User testUser2 = new User("nas@mail.ru", "Nas66", "Nas",
                LocalDate.of(1994, 6, 16));
        userDao.createUser(testUser2);
        User testUser3 = new User("shoori@mail.ru", "Demon", "Shoori",
                LocalDate.of(2019, 2, 16));
        userDao.createUser(testUser3);

        friendsDao.saveFriend(1,3);
        friendsDao.saveFriend(1, 2);
        friendsDao.saveFriend(2,1);
        friendsDao.saveFriend(2,3);
        friendsDao.saveFriend(3,2);
        friendsDao.saveFriend(3,1);

        assertEquals(2, userDao.getFriends(1).size());

    }

    @Test
    void getCommonFriends() {
        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));
        userDao.createUser(testUser);
        User testUser2 = new User("nas@mail.ru", "Nas66", "Nas",
                LocalDate.of(1994, 6, 16));
        userDao.createUser(testUser2);
        User testUser3 = new User("shoori@mail.ru", "Demon", "Shoori",
                LocalDate.of(2019, 2, 16));
        userDao.createUser(testUser3);

        friendsDao.saveFriend(1,3);
        friendsDao.saveFriend(1, 2);
        friendsDao.saveFriend(2,1);
        friendsDao.saveFriend(2,3);
        friendsDao.saveFriend(3,2);
        friendsDao.saveFriend(3,1);

        assertEquals(1, userDao.getCommonFriends(1,3).size());
        assertEquals(2, userDao.getCommonFriends(1,3).get(0).getId());
    }
}