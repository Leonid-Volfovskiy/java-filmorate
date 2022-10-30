package ru.yandex.practicum.filmorate.dao;

public interface LikesStorage {

    int saveLike(int filmId, int userId);

    int removeLike(int filmId, int userId);

}
