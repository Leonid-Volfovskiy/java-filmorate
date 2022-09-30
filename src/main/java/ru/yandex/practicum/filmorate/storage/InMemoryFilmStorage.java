package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Constants.MAX_DESCRIPTION_LENGTH;
import static ru.yandex.practicum.filmorate.Constants.OLD_RELEASE_DATE;

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
        filmValidation(film);
        film.setId(newFilmId());
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
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

    @Override
    public void deleteAllFilms () {
        films.clear();
    }

    @Override
    public Collection<Film> findAll() {
        return new ArrayList<>(films.values());
    }


    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(filmID)) {
            return films.get(filmID);
        } else {
            log.warn("Пользователь ввёл не существующий ID фильма");
            throw new NotFoundException("Фильм c таким ID = " + id + " не найден!");
        }
    }

    private boolean filmValidation(Film film) {
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
