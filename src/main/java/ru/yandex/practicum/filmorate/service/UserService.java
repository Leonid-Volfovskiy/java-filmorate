package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User user;
    private User friend;
    private List<User> friends;

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    private void makeUserObject (int id, int otherId) {
        user = getUserById(id);
        friend = getUserById(otherId);
    }

    public User addFriend (int id, int friendId) {
        makeUserObject(id, friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь " + user.getName() + " добавлен в список друзей "
                + friend.getName());
        return user;
    }

    public User deleteFriend (int id, int friendId) {
        makeUserObject(id, friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        log.info("Пользователь " + user.getName() + " удален из списка друзей "
                + friend.getName());
        return user;
    }

    public List<User> listOfFriendsByID (int id) {
        user = getUserById(id);
        friends = new ArrayList<>();

        for (int friendsID: user.getFriends()) {
            friend = getUserById(friendsID);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> listOfCommonFriends (int id, int otherId) {
        makeUserObject(id, otherId);
        friends = new ArrayList<>();

        for (int friendsId : user.getFriends()) {
            if (friend.getFriends().contains(friendsId)) {
                friends.add(getUserById(friendsId));
            }
        }
        return friends;
    }

}
