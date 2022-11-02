package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.*;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private User prepareUserFromBd(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    @Override
    public User createUser (User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        final String qs = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int result = jdbcTemplate.update(qs, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        if (result != 1) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public void deleteAllUsers() {
        String sqlQuery = "delete from users";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<User> findAllUsers() {
        final String sqlQuery = "select * from users";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::prepareUserFromBd);
        if (users.size() == 0) {
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public User getUserById(int id) throws NotFoundException{
        final String sqlQuery = "select * from users where user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::prepareUserFromBd, id);
        if (users.size() == 0) {
            log.debug(String.format("Пользователь %d не найден.", id));
            throw new NotFoundException("Пользователь не найден!");
        }
        return users.get(0);
    }

    @Override
    public List<User> getFriends(int userId) {
        String qs = "SELECT * FROM USERS, FRIENDS " +
                "WHERE USERS.USER_ID = FRIENDS.FRIEND_ID " +
                "AND FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(qs, this::prepareUserFromBd, userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String qs = "SELECT * FROM USERS u, FRIENDS f, " +
                "FRIENDS o WHERE u.USER_ID = f.FRIEND_ID " +
                "AND u.USER_ID = o.FRIEND_ID " +
                "AND f.USER_ID = ? AND o.USER_ID = ?";
        return jdbcTemplate.query(qs, this::prepareUserFromBd, userId, friendId);
    }

    @Override
    public int deleteUser(int id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sqlQuery, id);
    }
}
