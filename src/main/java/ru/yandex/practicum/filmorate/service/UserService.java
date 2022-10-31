package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public User create (User user) {
        return userStorage.createUser(user);
    }
    public User update (User user) {
        return userStorage.updateUser(user);
    }
    public void deleteAllUsers () {
        userStorage.deleteAllUsers();
    }
    public List<User> findAll() {
        return new ArrayList<>(userStorage.findAllUsers());
    }
    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend (int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен.");
        }
        friendsStorage.saveFriend(userId, friendId);
    }

    public void deleteFriend (int userId, int friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    }

    public List<User> getListOfFriendsByID(int userId) {
        return userStorage.getFriends(userId);
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
