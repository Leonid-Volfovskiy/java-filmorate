package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    public User create (User user) {
        return userStorage.create(user);
    }
    public User update (User user) {
        return userStorage.update(user);
    }
    public void deleteAllUsers () {
        userStorage.deleteAllUsers();
    }
    public List<User> findAll() {
        return new ArrayList<>(userStorage.findAll());
    }
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addFriend (int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.info("Пользователь " + user.getName() + " добавлен в список друзей "
                + friend.getName());
        return user;
    }

    public User deleteFriend (int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.info("Пользователь " + user.getName() + " удален из списка друзей "
                + friend.getName());
        return user;
    }

    public List<User> getListOfFriendsByID(int id) {
        User user = userStorage.getUserById(id);
        log.info("Список друзей пользователя " + user.getName());
        return user.getFriends()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> listOfCommonFriends (int id, int otherId) {
        User user = getUserById(id);
        User friend = getUserById(otherId);
        log.info("Список общих друзей пользователей");
        return user.getFriends()
                .stream()
                .filter(f -> friend.getFriends().contains(f))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

}
