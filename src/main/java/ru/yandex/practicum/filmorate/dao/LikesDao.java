package ru.yandex.practicum.filmorate.dao;

public interface LikesDao {

    int saveLike(int filmId, int userId);

    int removeLike(int filmId, int userId);

}
