package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;
import java.util.List;

public interface LikesDao {

    int saveLike(int filmId, int userId);

    int removeLike(int filmId, int userId);

    List<Like> getAllLikes();
}
