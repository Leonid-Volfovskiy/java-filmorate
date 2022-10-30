package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenresStorage genresStorage;

    @Autowired
    public GenreService(GenresStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    public Genre getById(int id) {
        if (id < 0) {
            throw new NotFoundException("Неверно передан ID Genre.");
        }
        return genresStorage.getById(id);
    }

    public List<Genre> getAll() {
        return genresStorage.getAllGenres();
    }
}
