package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilmDbStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;

    public Film prepareFilmFromBd(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaStorage.getById(rs.getInt("RAITING_ID")));
        film.setRate(rs.getInt("RATE"));
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", Date.valueOf(film.getReleaseDate()));
        values.put("DURATION", film.getDuration());
        values.put("RAITING_ID", film.getMpa().getId());
        values.put("RATE", film.getRate());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String qs = "UPDATE films SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, RAITING_ID = ? WHERE FILM_ID = ?";
        int result = jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result != 1) {
            throw new NotFoundException("Фильм не найден.");
        }
        return getFilmById(film.getId());
    }


    @Override
    public void deleteAllFilms() {
        String sqlQuery = "delete from FILMS";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Film> findAllFilms() {
        String qs = "SELECT * FROM FILMS AS f" /*+
                "LEFT JOIN FILMS_GENRES AS fg ON fg.film_id = f.FILM_ID" +
                "LEFT JOIN MPA AS m ON m.RAITING_ID = f.RAITING_ID" +
                "LEFT JOIN likes AS l on l.FILM_ID = f.FILM_ID" +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id"*/;
        return jdbcTemplate.query(qs, this::prepareFilmFromBd);
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        try {
            return jdbcTemplate.queryForObject(qs, this::prepareFilmFromBd, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }
}