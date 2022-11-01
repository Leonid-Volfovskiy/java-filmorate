package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenresDbDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        String qs = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(qs, this::prepareGenreFromBd, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String qs = "SELECT * FROM GENRES";
        return jdbcTemplate.query(qs, this::prepareGenreFromBd);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String qs = "SELECT * FROM GENRES g " +
                "INNER JOIN FILM_GENRES fg on g.GENRE_ID = fg.GENRE_ID" +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(qs, this::prepareGenreFromBd, filmId);
    }

    public void filmGenreUpdate(Integer filmId, List<Genre> genreList) {
        List<Genre> genresNoRepeat = genreList.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genresNoRepeat.get(i).getGenreId());
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

    private Genre prepareGenreFromBd(ResultSet rs, int rowNum) throws SQLException {
        int genreId = rs.getInt("GENRE_ID");
        String genreName = rs.getString("GENRE_NAME");
        return new Genre(genreId, genreName);
    }
}
