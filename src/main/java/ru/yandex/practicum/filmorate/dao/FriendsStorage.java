package ru.yandex.practicum.filmorate.dao;

public interface FriendsStorage {

    int saveFriend(int userId, int friendId);

    int deleteFriend(int userId, int friendId);

}
