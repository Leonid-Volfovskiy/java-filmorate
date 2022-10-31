package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.dao.LikesStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenresStorage genresStorage;
    private final LikesStorage likesStorage;

    public Film create (Film film) {
        Film newFilm = filmStorage.createFilm(film);
        if (film.getGenres() != null) {
            genresStorage.filmGenreUpdate(newFilm.getId(), film.getGenres());
        }
        newFilm.setGenres(genresStorage.getGenresByFilmId(newFilm.getId()));
        return newFilm;
    }
    public Film update (Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        genresStorage.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genresStorage.filmGenreUpdate(film.getId(), film.getGenres());
        }
        updatedFilm.setGenres(genresStorage.getGenresByFilmId(film.getId()));
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        List<Film> list = filmStorage.findAllFilms();
        for (Film filmFromList: list) {
            filmFromList.setGenres(genresStorage.getGenresByFilmId(filmFromList.getId()));
        }
        return list;
    }

    public void deleteAllFilms () {
        filmStorage.deleteAllFilms();
    }

    public Film getFilmById (int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        film.setGenres(genresStorage.getGenresByFilmId(film.getId()));
        return film;
    }

    public Film addLike (int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.setRate(film.getRate() + 1);
        likesStorage.saveLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.setRate(film.getRate() - 1);
        likesStorage.removeLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms (int count) {
        log.info("Список десяти самых популярных фильмов");
        return filmStorage.findAllFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }
}
