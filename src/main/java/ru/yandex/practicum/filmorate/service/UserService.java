package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FriendsDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDbStorageImpl userDbStorageImpl;
    private final FriendsDbStorageImpl friendsDbStorageImpl;

    public User create (User user) {
        return userDbStorageImpl.createUser(user);
    }
    public User update (User user) {
        return userDbStorageImpl.updateUser(user);
    }
    public void deleteAllUsers () {
        userDbStorageImpl.deleteAllUsers();
    }
    public List<User> findAll() {
        return new ArrayList<>(userDbStorageImpl.findAllUsers());
    }
    public User getUserById(int userId) {
        return userDbStorageImpl.getUserById(userId);
    }

    public void addFriend (int userId, int friendId) {
        if (userId < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен.");
        }
        friendsDbStorageImpl.saveFriend(userId, friendId);
    }

    public void deleteFriend (int userId, int friendId) {
        friendsDbStorageImpl.deleteFriend(userId, friendId);
    }

    public List<User> getListOfFriendsByID(int userId) {
        return userDbStorageImpl.getFriends(userId);
    }


    public List<User> listOfCommonFriends (int id, int otherId) {
        User user = getUserById(id);
        User friend = getUserById(otherId);
        log.info("Список общих друзей пользователей");
        return user.getFriends()
                .stream()
                .filter(f -> friend.getFriends().contains(f))
                .map(userDbStorageImpl::getUserById)
                .collect(Collectors.toList());
    }

}
