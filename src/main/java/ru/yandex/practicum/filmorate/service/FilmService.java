package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film create (Film film) {
        return filmStorage.create(film);
    }
    public Film update (Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return new ArrayList<>(filmStorage.findAll());
    }
    public void deleteAllFilms () {
        filmStorage.deleteAllFilms();
    }

    public Film getFilmById (int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film addLike (int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUserById(userId);
        film.getLikes().add(userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUserById(userId);
        film.getLikes().remove(userId);
        log.info("Пользователь " + user.getName() + " удалил лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms (int count) {
        return filmStorage.findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }


}
