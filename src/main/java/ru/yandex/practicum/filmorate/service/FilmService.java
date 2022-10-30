package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.GenresDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.LikesDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDbStorageImpl filmDbStorageImpl;
    private final UserDbStorageImpl userDbStorageImpl;
    private final GenresDbStorageImpl genreDbStorageImpl;
    private final LikesDbStorageImpl likesDbStorageImpl;

    public Film create (Film film) {
        Film newFilm = filmDbStorageImpl.createFilm(film);
        if (film.getGenres() != null) {
            genreDbStorageImpl.filmGenreUpdate(newFilm.getId(), film.getGenres());
        }
        newFilm.setGenres(genreDbStorageImpl.getGenresByFilmId(newFilm.getId()));
        return newFilm;
    }
    public Film update (Film film) {
        Film updatedFilm = filmDbStorageImpl.updateFilm(film);
        genreDbStorageImpl.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genreDbStorageImpl.filmGenreUpdate(film.getId(), film.getGenres());
        }
        updatedFilm.setGenres(genreDbStorageImpl.getGenresByFilmId(film.getId()));
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        List<Film> list = filmDbStorageImpl.findAllFilms();
        for (Film filmFromList: list) {
            filmFromList.setGenres(genreDbStorageImpl.getGenresByFilmId(filmFromList.getId()));
        }
        return list;
    }

    public void deleteAllFilms () {
        filmDbStorageImpl.deleteAllFilms();
    }

    public Film getFilmById (int filmId) {
        Film film = filmDbStorageImpl.getFilmById(filmId);
        film.setGenres(genreDbStorageImpl.getGenresByFilmId(film.getId()));
        return film;
    }

    public Film addLike (int filmId, int userId) {
        Film film = filmDbStorageImpl.getFilmById(filmId);
        User user = userDbStorageImpl.getUserById(userId);
        film.setRate(film.getRate() + 1);
        likesDbStorageImpl.saveLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = filmDbStorageImpl.getFilmById(filmId);
        User user = userDbStorageImpl.getUserById(userId);
        film.setRate(film.getRate() - 1);
        likesDbStorageImpl.removeLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms (int count) {
        log.info("Список десяти самых популярных фильмов");
        return filmDbStorageImpl.findAllFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }
}
