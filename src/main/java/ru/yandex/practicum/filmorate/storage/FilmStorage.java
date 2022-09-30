package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create (Film film);
    Film update (Film film);
    void deleteAllFilms ();
    Collection<Film> findAll();
    Film getFilmById(int id);

}
