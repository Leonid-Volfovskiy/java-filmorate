package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDao userDao;
    private final FriendsDao friendsDao;

    public User create (User user) {
        return userDao.createUser(user);
    }
    public User update (User user) {
        return userDao.updateUser(user);
    }
    public void deleteAllUsers () {
        userDao.deleteAllUsers();
    }
    public List<User> findAll() {
        return new ArrayList<>(userDao.findAllUsers());
    }
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public void addFriend (int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен.");
        }
        friendsDao.saveFriend(userId, friendId);
    }

    public void deleteFriend (int userId, int friendId) {
        friendsDao.deleteFriend(userId, friendId);
    }

    public List<User> getListOfFriendsByID(int userId) {
        return userDao.getFriends(userId);
    }


    public List<User> listOfCommonFriends (int userId, int friendId) {
        return userDao.getCommonFriends(userId, friendId);
    }

}
