package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDaoTest {

    private final FilmDao filmDao;
    private final LikesDao likesDao;
    private final UserDao userDao;

    @Test
    void createFilm() {
        Film testFilm = new Film("HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4
                , new Mpa(1, "R"));

        filmDao.createFilm(testFilm);

        assertEquals("R", testFilm.getMpa().getName());
        assertEquals(1, testFilm.getId());
        assertEquals(1, filmDao.findAllFilms().size());

        System.out.println(testFilm);
    }

    @Test
    void updateFilm() {
        Film testFilm = new Film("HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4
                , new Mpa(1, "R"));

        filmDao.createFilm(testFilm);


        Film updatedFilm = new Film(1,
                "HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4,
                new Mpa(1, "NC-17"));
        filmDao.updateFilm(updatedFilm);
        assertEquals("NC-17", updatedFilm.getMpa().getName());
        assertEquals(1, updatedFilm.getId());
        assertEquals(1, filmDao.findAllFilms().size());

        System.out.println(updatedFilm);
    }

    @Test
    void findAllFilms() {
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
        assertEquals(2, filmDao.findAllFilms().size());
    }

    @Test
    void getFilmById() {
        Film testFilm = new Film("HP"
                , LocalDate.of(2022, 1, 1)
                , "test film 1"
                , 100
                , 4
                , new Mpa(1, "R"));

        filmDao.createFilm(testFilm);

        Film testGet = filmDao.getFilmById(1);
        assertEquals("HP", testGet.getName());
    }

    @Test
    void getPopularFilms() {
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

        likesDao.saveLike(testFilm.getId(), testUser.getId());
        likesDao.saveLike(testFilm2.getId(), testUser.getId());

        List<Film> popularFilm = filmDao.getPopularFilms(2);
        System.out.println(popularFilm.get(0));
        assertEquals(popularFilm.size(), 2);
    }

    @Test
    void deleteFilmById() {
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

        assertEquals(2, filmDao.findAllFilms().size());
        filmDao.deleteFilmById(2);
        assertEquals(1, filmDao.findAllFilms().size());
        filmDao.deleteFilmById(1);
        assertEquals(0, filmDao.findAllFilms().size());
    }

}