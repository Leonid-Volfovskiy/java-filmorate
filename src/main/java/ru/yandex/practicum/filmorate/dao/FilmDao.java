package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    Film createFilm(Film film);
    Film updateFilm(Film film);
    void deleteAllFilms ();
    List<Film> findAllFilms();
    Film getFilmById(int id);
    List<Film> getPopularFilms(int count);

}
