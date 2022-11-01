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
        String qs = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        return jdbcTemplate.update(qs, userId, friendId);
    }

    @Override
    public int deleteFriend(int userId, int friendId) {
        String qs = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        return jdbcTemplate.update(qs, userId, friendId);
    }

}
