package ru.yandex.practicum.filmorate.dao;

public interface FriendsDao {

    int saveFriend(int userId, int friendId);

    int deleteFriend(int userId, int friendId);

}
