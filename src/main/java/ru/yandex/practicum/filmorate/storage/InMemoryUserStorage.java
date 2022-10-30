package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private int newUserId() {
        return ++userId;
    }

    @Override
    public User createUser(User user) {
        user.setId(newUserId());
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден.");
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя с ID " + user.getId() + " обновлены.");
        return user;
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public List<User> getFriends(int userId) {
        return null;
    }

    @Override
    public List<User> getCorporateFriends(int userId, int friendId) {
        return null;
    }
}
