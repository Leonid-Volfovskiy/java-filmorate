package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsStorage;

@Component
@AllArgsConstructor
public class FriendsDbStorageImpl implements FriendsStorage {
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
