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
        Film newFilm = null;
        if (filmValidation(film)) {
            film.setId(newFilmId());
            films.put(film.getId(), film);
            newFilm = films.get(film.getId());
            log.debug("Добавлен новый фильм: {}", film);
        }
        return newFilm;
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
            throw new NotFoundException("Фильма с таким Id = " + film.getId() + " нет");
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
        return films.get(filmID);
    }

    private boolean filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(OLD_RELEASE_DATE)){
            log.warn("Пользователь ввёл слишком старый фильм");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        return true;
    }

}
