package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

@Repository
@RequiredArgsConstructor
public class FriendsDbDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public int saveFriend(int userId, int friendId) {
        String qs = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        return jdbcTemplate.update(qs, userId, friendId);
    }

    @Override
    public int deleteFriend(int userId, int friendId) {
        String qs = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(qs, userId, friendId);
    }

}
