package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendsDaoTest {

    private final FriendsDao friendsDao;
    private final UserDao userDao;

    @Test
    void saveFriend() {
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
        friendsDao.saveFriend(1,2);

        assertEquals(2, userDao.getFriends(1).size());
    }

    @Test
    void deleteFriend() {
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
        friendsDao.saveFriend(1,2);

        friendsDao.deleteFriend(1,3);
        assertEquals(1, userDao.getFriends(1).size());
    }
}