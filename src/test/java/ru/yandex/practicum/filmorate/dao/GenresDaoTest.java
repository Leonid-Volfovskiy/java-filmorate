package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenresDaoTest {

    private final GenresDao genresDao;

    @Test
    void getById() {
        assertEquals("Комедия", genresDao.getById(1).getGenreName());
        assertEquals("Боевик", genresDao.getById(6).getGenreName());
    }

    @Test
    void getAllGenres() {
        assertEquals(6, genresDao.getAllGenres().size());
    }
}