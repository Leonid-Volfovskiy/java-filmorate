package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikesDbDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int saveLike(int filmId, int userId) {
        String qs = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    @Override
    public int removeLike(int filmId, int userId) {
        String qs = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    @Override
    public List<Like> getAllLikes() {
        String qs = "SELECT * FROM likes";
        return jdbcTemplate.query(qs, this::prepareLikeFromBd);
    }

    private void updateRate(int filmId) {
        jdbcTemplate.update("UPDATE films f " +
                "SET rate = (" +
                "SELECT COUNT(l.user_id) " +
                "FROM likes l " +
                "WHERE l.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }

    private Like prepareLikeFromBd(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        int userId = rs.getInt("user_id");
        return new Like(filmId, userId);
    }
}
