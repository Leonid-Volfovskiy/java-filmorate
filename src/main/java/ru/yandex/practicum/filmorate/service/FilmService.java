package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final GenresDao genresDao;

    public Film create (Film film) {
        Film newFilm = filmDao.createFilm(film);
        if (film.getGenres() != null) {
            genresDao.filmGenreUpdate(newFilm.getId(), film.getGenres());
        }
        newFilm.setGenres(genresDao.getGenresByFilmId(newFilm.getId()));
        log.info("Фильм сохранен: " + newFilm.getId() + " .");
        return newFilm;
    }
    public Film update (Film film) {
        Film updatedFilm = filmDao.updateFilm(film);
        genresDao.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            genresDao.filmGenreUpdate(film.getId(), film.getGenres());
        }
        updatedFilm.setGenres(genresDao.getGenresByFilmId(film.getId()));
        log.info("Данные фильма обновлены: " + updatedFilm.getId() + " .");
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        List<Film> list = filmDao.findAllFilms();
        log.info("Получен список всех фильмов.");
        return list;
    }

    public void deleteAllFilms () {
        log.info("Все фильмы удалены.");
        filmDao.deleteAllFilms();
    }

    public int deleteFilmById(int id) {
        log.info("Фильм удален.");
        return filmDao.deleteFilmById(id);
    }

    public Film getFilmById (int filmId) {
        Film film = filmDao.getFilmById(filmId);
        film.setGenres(genresDao.getGenresByFilmId(film.getId()));
        log.info("Получен фильм с идентификатором " + filmId + ".");
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
