package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenresDao genresDao;

    public Genre getById(int id) {
        if (id < 0) {
            throw new NotFoundException("Неверно передан ID Genre.");
        }
        return genresDao.getById(id);
    }

    public List<Genre> getAll() {
        return genresDao.getAllGenres();
    }
}
