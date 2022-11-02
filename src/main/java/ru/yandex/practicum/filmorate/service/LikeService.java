package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final UserDao userDao;
    private final LikesDao likesDao;
    private final FilmDao filmDao;

    public Film addLike (int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        likesDao.saveLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " поставил лайк фильму " + film.getName());
        return film;
    }

    public Film deleteLike (int filmId, int userId) {
        Film film = filmDao.getFilmById(filmId);
        User user = userDao.getUserById(userId);
        likesDao.removeLike(filmId, userId);
        log.info("Пользователь " + user.getName() + " удалил лайк у фильма " + film.getName());
        return film;
    }
}
