package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film getFilmById (int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film addLike (int filmId, int userId) {
        Film film = null;
        User user = null;

        if (filmId > 0 && userId >0) {
            film = filmStorage.getFilmById(filmId);
            user = userService.getUserById(userId);
        }
        if (film != null) {
            film.getLikes().add(userId);
        }
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = null;
        User user = null;

        if (filmId > 0 && userId >0) {
            film = filmStorage.getFilmById(filmId);
            user = userService.getUserById(userId);
        }

        if (film != null) {
            film.getLikes().remove(userId);
        }
        return film;
    }

    public List<Film> getPopularFilms (int count) {
        if (count < 0) {
            throw new ValidationException("Некорректное количество фильмов = " + count);
        }

        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f1.getLikes().size() - f2.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
