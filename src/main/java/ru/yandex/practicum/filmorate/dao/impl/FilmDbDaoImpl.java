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
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmorate.dao.impl.GenresDbDaoImpl.createGenreByRs;

@Repository
@RequiredArgsConstructor
public class FilmDbDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;

    private Film prepareFilmFromBd(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getDate("release_date").toLocalDate(),
                rs.getString("description"),
                rs.getInt("duration"),
                rs.getInt("rate"),
                new Mpa(rs.getInt("id"), rs.getString("mpa_name")));
    }

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("id", film.getMpa().getId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String qs = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, id = ? WHERE film_id = ?";
        int result = jdbcTemplate.update(qs, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result != 1) {
            throw new NotFoundException("Фильм не найден.");
        }
        return getFilmById(film.getId());
    }


    @Override
    public void deleteAllFilms() {
        String sqlQuery = "delete from films";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Film> findAllFilms() {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN mpa m ON m.id = f.id;";
        List<Film> films = jdbcTemplate.query(qs, this::prepareFilmFromBd);
        addGenresToFilms(films);
        return films;
    }

    @Override
    public Film getFilmById(int id) {
        String qs = "SELECT * FROM films AS f " +
                "LEFT JOIN mpa m ON m.id = f.id " +
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
                "LEFT JOIN mpa m ON m.id = f.id " +
                "LEFT OUTER JOIN likes l on f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) " +
                "DESC LIMIT ?;";

        List<Film> films = jdbcTemplate.query(qs, this::prepareFilmFromBd, count);
        addGenresToFilms(films);
        return films;
    }

    @Override
    public int deleteFilmById(int id) {
        final String qs = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(qs, id);
    }

    private void addGenresToFilms(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genres g, film_genres fg WHERE fg.genre_id" +
                " = g.genre_id AND fg.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.addGenreToFilm(createGenreByRs(rs));
        });
    }
}