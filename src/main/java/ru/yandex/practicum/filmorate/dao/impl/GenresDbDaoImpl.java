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
        String qs = "SELECT * FROM genres WHERE genre_id = ?";
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
                "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
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
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
    }

    private Genre prepareGenreFromBd(ResultSet rs, int rowNum) throws SQLException {
        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre_name");
        return new Genre(genreId, genreName);
    }

    public static Genre createGenreByRs(ResultSet rs) throws SQLException {
        return Genre.builder()
                .genreId(rs.getInt("genre_id"))
                .genreName(rs.getString("genre_name"))
                .build();
    }
}
