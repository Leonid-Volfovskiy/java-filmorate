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
        String qs = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(qs, this::prepareGenreFromBd, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String qs = "SELECT * FROM genres";
        return jdbcTemplate.query(qs, this::prepareGenreFromBd);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String qs = "SELECT * FROM genres g " +
                "INNER JOIN films_genres fg on g.id = fg.id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(qs, this::prepareGenreFromBd, filmId);
    }

    public void filmGenreUpdate(Integer filmId, List<Genre> genreList) {
        List<Genre> genresNoRepeat = genreList.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO films_genres (film_id, id) VALUES (?, ?)",
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
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", filmId);
    }

    private Genre prepareGenreFromBd(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String genreName = rs.getString("genre_name");
        return new Genre(id, genreName);
    }

    public static Genre createGenreByRs(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
