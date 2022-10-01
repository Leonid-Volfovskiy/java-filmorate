package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private int filmID = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private int newFilmId() {
        return ++filmID;
    }

    @Override
    public Film create(Film film) {
        film.setId(newFilmId());
        films.put(film.getId(), film);
        Film newFilm = films.get(film.getId());
        log.debug("Добавлен новый фильм: {}", film);
        return newFilm;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        log.info("Фильм с номером " + film.getId() + " обновлен.");
        return film;
    }

    @Override
    public void deleteAllFilms () {
        films.clear();
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }


    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
    }

}
