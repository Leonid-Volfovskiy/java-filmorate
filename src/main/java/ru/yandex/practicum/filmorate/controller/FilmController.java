package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int filmID = 0;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate OLD_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    private int newFilmId() {
        return ++filmID;
    }

    protected void clearFilms() {
        films.clear();
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        filmValidation(film);
        film.setId(newFilmId());
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        Film updatedFilm = null;
        if (filmValidation(film) && films.containsKey(film.getId())) {
            updatedFilm = films.get(film.getId());
            updatedFilm.setName(film.getName());
            updatedFilm.setDescription(film.getDescription());
            updatedFilm.setReleaseDate(film.getReleaseDate());
            updatedFilm.setDuration(film.getDuration());
            log.debug("Фильм обновлен: {}", updatedFilm);
        } else {
            log.warn("Запрос к эндпоинту PUT не обработан. Пользователь пытался обновить несуществующего User-a");
            throw new ValidationException("Фильма с таким Id = " + film.getId() + " нет");
        }
        return updatedFilm;
    }


    private boolean filmValidation(Film film) {
        if (film == null) {
            log.warn("Пользователь передал пустой объект вместо информации о фильме");
            throw new ValidationException("Фильм не может быть пустым");
        }
        if(film.getName() == null || film.getName().isBlank()) {
            log.warn("Пользователь ввёл пустое название фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Пользователь ввёл слишком длинное описание фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(OLD_RELEASE_DATE)){
            log.warn("Пользователь ввёл слишком старый фильм");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Пользователь ввёл фильм с отрицательной продолжительностью");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }
}

