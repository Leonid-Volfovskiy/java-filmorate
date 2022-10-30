package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);
    User updateUser(User user);
    void deleteAllUsers ();
    List<User> findAllUsers();
    User getUserById(int id);
    List<User> getFriends(int userId);
    List<User> getCorporateFriends(int userId, int friendId);
}
