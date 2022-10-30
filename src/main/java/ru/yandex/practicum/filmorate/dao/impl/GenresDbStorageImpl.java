package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GenresDbStorageImpl implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        String qs = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(qs, this::makeGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String qs = "SELECT * FROM GENRES";
        return jdbcTemplate.query(qs, this::makeGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String qs = "SELECT * FROM GENRES g " +
                "INNER JOIN FILM_GENRES fg on g.GENRE_ID = fg.GENRE_ID" +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(qs, this::makeGenre, filmId);
    }

    public void filmGenreUpdate(Integer filmId, List<Genre> genreList) {
        List<Genre> genresNoRepeat = genreList.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genresNoRepeat.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genresNoRepeat.size();
                    }
                });
    }

    @Override
    public void deleteGenresByFilmId(int filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int genreId = rs.getInt("GENRE_ID");
        String genreName = rs.getString("GENRE_NAME");
        return new Genre(genreId, genreName);
    }
}
