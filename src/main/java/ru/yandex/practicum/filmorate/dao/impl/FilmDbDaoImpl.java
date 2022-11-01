package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FilmDbDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;

    private Film prepareFilmFromBd(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaDao.getById(rs.getInt("RAITING_ID")));
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
        values.put("RAITING_ID", film.getMpa().getRaitingId());
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
                film.getDuration(), film.getMpa().getRaitingId(), film.getId());
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
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA m ON m.RAITING_ID = f.RAITING_ID;";
        List<Film> allFilms = jdbcTemplate.query(qs, this::prepareFilmFromBd);
        return allFilms;
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA m ON m.RAITING_ID = f.RAITING_ID " +
                "WHERE f.film_id = ?;";
        try {
            return jdbcTemplate.queryForObject(qs, this::prepareFilmFromBd, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        final String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN MPA m ON m.RAITING_ID = f.RAITING_ID " +
                "LEFT OUTER JOIN LIKES l on f.FILM_ID = l.FILM_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(l.FILM_ID) " +
                "DESC LIMIT ?;";

        List<Film> popFilm = jdbcTemplate.query(qs, this::prepareFilmFromBd, count);
        return popFilm;
    }

    @Override
    public int deleteFilmById(int id) {
        final String qs = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(qs, id);
    }
}