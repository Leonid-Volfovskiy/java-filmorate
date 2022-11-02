package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikeDaoTest {

    private final LikesDao likesDao;
    private final UserDao userDao;
    private final FilmDao filmDao;

    @Test
    void saveLike() {
        Film testFilm = new Film("HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4
                , new Mpa(1, "R"));

        filmDao.createFilm(testFilm);

        Film testFilm2 = new Film("HP2"
                , LocalDate.of(2022, 2, 2)
                , "test film 2"
                , 101
                , 2
                , new Mpa(2, "PG"));

        filmDao.createFilm(testFilm2);

        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));

        userDao.createUser(testUser);

        User testUser2 = new User("nas@mail.ru", "Nas66", "Nas",
                LocalDate.of(1994, 6, 16));
        userDao.createUser(testUser2);

        likesDao.saveLike(testFilm.getId(), testUser.getId());
        likesDao.saveLike(testFilm2.getId(), testUser2.getId());

        List<Film> likesFilm = filmDao.getPopularFilms(10);

        assertEquals(likesFilm.size(), 2);
    }

    @Test
    void removeLike() {
        Film testFilm = new Film("HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4
                , new Mpa(1, "R"));

        filmDao.createFilm(testFilm);

        Film testFilm2 = new Film("HP2"
                , LocalDate.of(2022, 2, 2)
                , "test film 2"
                , 101
                , 2
                , new Mpa(2, "PG"));

        filmDao.createFilm(testFilm2);

        User testUser = new User("leo@mail.ru", "Leo99", "Leo",
                LocalDate.of(1992, 6, 27));

        userDao.createUser(testUser);

        User testUser2 = new User("nas@mail.ru", "Nas66", "Nas",
                LocalDate.of(1994, 6, 16));
        userDao.createUser(testUser2);

        likesDao.saveLike(testFilm.getId(), testUser.getId());
        likesDao.saveLike(testFilm2.getId(), testUser2.getId());

        assertEquals(2, likesDao.getAllLikes().size());
        likesDao.removeLike(testFilm2.getId(), testUser2.getId());

        assertEquals(1, likesDao.getAllLikes().size());
    }
}