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
        log.info("Пользователь создан: " + user.getId() + " .");
        return userDao.createUser(user);
    }
    public User update (User user) {
        return userDao.updateUser(user);
    }
    public void deleteAllUsers () {
        log.info("Все пользователи удалены.");
        userDao.deleteAllUsers();
    }

    public int deleteUser(int id) {
        log.info("Пользователь удален: " + id + " .");
        return userDao.deleteUser(id);
    }

    public List<User> findAll() {
        log.info("Получен список всех пользователей.");
        return new ArrayList<>(userDao.findAllUsers());
    }
    public User getUserById(int userId) {
        log.info("Получен пользователь с идентификатором " + userId + ".");
        return userDao.getUserById(userId);
    }

    public void addFriend (int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен.");
        }
        log.info("Пользователь " + userId + " добавил в друзья пользователя " + friendId + ".");
        friendsDao.saveFriend(userId, friendId);
    }

    public void deleteFriend (int userId, int friendId) {
        log.info("Пользователь " + userId + " удалил из друзей пользователя " + friendId + ".");
        friendsDao.deleteFriend(userId, friendId);
    }

    public List<User> getListOfFriendsByID(int userId) {
        log.info("Получен список друзей пользователя " + userId + ".");
        return userDao.getFriends(userId);
    }


    public List<User> listOfCommonFriends (int userId, int friendId) {
        log.info("Получен список общих друзей пользователя " + userId + " и пользователя " + friendId + ".");
        return userDao.getCommonFriends(userId, friendId);
    }

}
