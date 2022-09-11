package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    private int filmID = 0;
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
    public Film create(@RequestBody Film film) throws ValidationException {
        Film newFilm = null;
        if (filmValidation(film)) {
            newFilm = Film.builder()
                    .id(newFilmId())
                    .name(film.getName())
                    .description(film.getDescription())
                    .releaseDate(film.getReleaseDate())
                    .duration(film.getDuration())
                    .build();
            films.put(newFilm.getId(), newFilm);
            log.debug("Добавлен новый фильм: {}", newFilm);
        }
        return newFilm;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
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
            throw new ValidationException("Фильма с таким Id = " + film.getId() + " нет.");
        }
        return updatedFilm;
    }


    private boolean filmValidation(Film film) throws ValidationException {
        if(film.getName() == null || film.getName().isBlank()) {
            log.warn("Пользователь ввёл пустое название фильма.");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Пользователь ввёл слишком длинное описание фильма.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))){
            log.warn("Пользователь ввёл слишком старый фильм");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration().toMinutes() <= 0) {
            log.warn("Пользователь ввёл фильм с отрицательной продолжительностью");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }



}

/*
название не может быть пустым;
максимальная длина описания — 200 символов;
дата релиза — не раньше 28 декабря 1895 года;
продолжительность фильма должна быть положительной.
 */