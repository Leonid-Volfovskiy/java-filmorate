package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

@Component
@AllArgsConstructor
@Slf4j
public class UserDbStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public static User getUserFromBd(ResultSet rs, int rowNumber) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }

    @Override
    public User createUser (User user) {
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
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
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::getUserFromBd);
        if (users.size() == 0) {
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public User getUserById(int id) throws NotFoundException{
        final String sqlQuery = "select * from USERS where user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::getUserFromBd, id);
        if (users.size() == 0) {
            log.debug(String.format("Пользователь %d не найден.", id));
            throw new NotFoundException("Пользователь не найден!");
        }
        return users.get(0);
    }

    @Override
    public List<User> getFriends(int userId) {
        String qs = "SELECT u.* FROM FRIENDS fr " +
                "JOIN users u on fr.friend_id = u.user_id " +
                "WHERE fr.user_id = ?;";
        return jdbcTemplate.query(qs, UserDbStorageImpl::getUserFromBd, userId);
    }

    @Override
    public List<User> getCorporateFriends(int userId, int friendId) {
        String qs = "SELECT u.* FROM FRIENDS fr " +
                "JOIN users u ON fr.friend_id = u.user_id " +
                "WHERE u.user_id = ? " +
                "UNION " +
                "SELECT u.* FROM FRIENDS fr " +
                "JOIN users u ON fr.friend_id = u.user_id " +
                "WHERE fr.user_id = ?;";
        return jdbcTemplate.query(qs, UserDbStorageImpl::getUserFromBd, userId, friendId);
    }
}
