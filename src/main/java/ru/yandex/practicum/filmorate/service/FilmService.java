package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenresDao genresDao;
    private final LikesDao likesDao;

    public Film create (Film film) {
        Film newFilm = filmDao.createFilm(film);
        if (film.getGenres() != null) {
            genresDao.filmGenreUpdate(newFilm.getId(), film.getGenres());
        }
        newFilm.setGenres(genresDao.getGenresByFilmId(newFilm.getId()));
        return newFilm;
    }
    public Film update (Film film) {
        Film updatedFilm = filmDao.updateFilm(film);
        genresDao.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genresDao.filmGenreUpdate(film.getId(), film.getGenres());
        }
        updatedFilm.setGenres(genresDao.getGenresByFilmId(film.getId()));
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        List<Film> list = filmDao.findAllFilms();
        for (Film filmFromList: list) {
            filmFromList.setGenres(genresDao.getGenresByFilmId(filmFromList.getId()));
        }
        return list;
    }

    public void deleteAllFilms () {
        filmDao.deleteAllFilms();
    }

    public Film getFilmById (int filmId) {
        Film film = filmDao.getFilmById(filmId);
        film.setGenres(genresDao.getGenresByFilmId(film.getId()));
        return film;
    }

    public Film addLike (int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.setRate(film.getRate() + 1);
        likesDao.saveLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        film.setRate(film.getRate() - 1);
        likesDao.removeLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк у фильма " + film.getName());
        return film;
    }

    public List<Film> getPopularFilms(int countFilms) {
        List<Film> films = filmDao.getPopularFilms(countFilms);
        log.info("Список из " + countFilms + " самых популярных фильмов.");
        return films.stream()
                .sorted(this::compare)
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getRate() - f0.getRate();
    }
}
