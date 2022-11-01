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
import java.time.LocalDate;
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
        final String qs = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        int result = jdbcTemplate.update(qs, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        if (result != 1) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public void deleteAllUsers() {
        String sqlQuery = "delete from USERS";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<User> findAllUsers() {
        final String sqlQuery = "select * from USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::prepareUserFromBd);
        if (users.size() == 0) {
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public User getUserById(int id) throws NotFoundException{
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::prepareUserFromBd, id);
        if (users.size() == 0) {
            log.debug(String.format("Пользователь %d не найден.", id));
            throw new NotFoundException("Пользователь не найден!");
        }
        return users.get(0);
    }

    @Override
    public List<User> getFriends(int userId) {
        String qs = "SELECT u.* FROM FRIENDS fr " +
                "JOIN users u on fr.FRIEND_ID = u.USER_ID " +
                "WHERE fr.USER_ID = ?;";
        return jdbcTemplate.query(qs, this::prepareUserFromBd, userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String qs = "SELECT u.* FROM FRIENDS f " +
                "JOIN users u ON f.FRIEND_ID = u.USER_ID " +
                "WHERE f.USER_ID = ? OR f.USER_ID = ? " +
                "GROUP BY f.FRIEND_ID " +
                "HAVING COUNT(f.USER_ID) > 1;";
        return jdbcTemplate.query(qs, this::prepareUserFromBd, userId, friendId);
    }

    @Override
    public int deleteUser(int id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, id);
    }
}