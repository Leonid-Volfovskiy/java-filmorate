package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    private List<User> friends;
    public User create (User user) {
        return userStorage.create(user);
    }
    public User update (User user) {
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
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(id);
            log.info("Пользователь " + user.getName() + " добавлен в список друзей "
                    + friend.getName());
        } else if (user == null) {
            throw new NotFoundException("Пользователя с таким Id = " + user.getId() + " нет");
        } else {
            throw new NotFoundException("Пользователя с таким Id = " + friend.getId() + " нет");
        }

        return user;
    }

    public User deleteFriend (int id, int friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);

        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
            log.info("Пользователь " + user.getName() + " удален из списка друзей "
                    + friend.getName());
        } else if (user == null) {
            throw new NotFoundException("Пользователя с таким Id = " + user.getId() + " нет");
        } else {
            throw new NotFoundException("Пользователя с таким Id = " + friend.getId() + " нет");
        }

        return user;
    }

    public List<User> listOfFriendsByID (int id) {
        User user = getUserById(id);
        friends = new ArrayList<>();
        for (int friendsID: user.getFriends()) {
            User friend = getUserById(friendsID);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> listOfCommonFriends (int id, int otherId) {
        User user = getUserById(id);
        User friend = getUserById(otherId);
        friends = new ArrayList<>();
        for (int friendsId : user.getFriends()) {
            if (friend.getFriends().contains(friendsId)) {
                friends.add(getUserById(friendsId));
            }
        }
        return friends;
    }

}
