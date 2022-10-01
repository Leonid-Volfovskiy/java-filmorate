package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    public User create (User user) {
        userValidation(user);
        return userStorage.create(user);
    }
    public User update (User user) {
        userValidation(user);
        return userStorage.update(user);
    }
    public void deleteAllUsers () {
        userStorage.deleteAllUsers();
    }
    public Collection<User> findAll() {
        return userStorage.findAll();
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

    public List<User> getlistOfFriendsByID(int id) {
        User user = userStorage.getUserById(id);
        List<User> friends = new ArrayList<>();
        for (int friendsID: user.getFriends()) {
            User friend = userStorage.getUserById(friendsID);
            friends.add(friend);
        }
        log.info("Список друзей пользователя " + user.getName());
        return friends;
    }

    public List<User> listOfCommonFriends (int id, int otherId) {
        User user = getUserById(id);
        User friend = getUserById(otherId);
        List<User> friends = new ArrayList<>();
        for (int friendsId : user.getFriends()) {
            if (friend.getFriends().contains(friendsId)) {
                User commonFriend = userStorage.getUserById(friendsId);
                friends.add(commonFriend);
            }
        }
        log.info("Список общих друзей пользователей");
        return friends;
    }

    private void userValidation(User user){
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пользователь ввёл пустое имя");
            user.setName(user.getLogin());
        }
    }

}
