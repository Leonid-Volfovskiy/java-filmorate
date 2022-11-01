package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikesDao;

@Repository
@RequiredArgsConstructor
public class LikesDbDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int saveLike(int filmId, int userId) {
        String qs = "INSERT INTO LIKES (film_id, user_id) VALUES (?, ?)";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    @Override
    public int removeLike(int filmId, int userId) {
        String qs = "DELETE FROM LIKES WHERE user_id = ? AND film_id = ?";
        updateRate(filmId);
        return jdbcTemplate.update(qs, filmId, userId);
    }

    private void updateRate(int filmId) {
        jdbcTemplate.update("UPDATE FILMS f " +
                "SET RATE = (" +
                "SELECT COUNT(l.user_id) " +
                "FROM likes l " +
                "WHERE l.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }
}
