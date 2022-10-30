package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresStorage {

    Genre getById(int id);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(int filmId);

    void filmGenreUpdate(Integer filmId, List<Genre> genreList);

    void deleteGenresByFilmId(int filmId);
}
