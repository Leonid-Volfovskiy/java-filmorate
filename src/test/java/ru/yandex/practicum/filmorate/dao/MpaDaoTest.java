package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoTest {

    private final MpaDao mpaDao;

    @Test
    void getById() {
        assertEquals("G", mpaDao.getById(1).getMpaName());
        assertEquals("NC-17", mpaDao.getById(5).getMpaName());
    }

    @Test
    void getAllMpas() {
        assertEquals(5, mpaDao.getAllMpas().size());
    }
}