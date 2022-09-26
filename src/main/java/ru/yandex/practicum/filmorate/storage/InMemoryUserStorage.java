package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private int newUserId() {
        return ++userId;
    }

    @Override
    public User create(User user) {
        userValidation(user);
        user.setId(newUserId());
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        User updatedUser = null;
        if (userValidation(user) && users.containsKey(user.getId())) {
            updatedUser = users.get(user.getId());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setName(user.getName());
            updatedUser.setBirthday(user.getBirthday());
            log.debug("Пользователь обновлен: {}", updatedUser);
        } else {
            throw new NotFoundException("Пользователя с таким Id = " + user.getId() + " нет");
        }
        return updatedUser;
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.warn("Пользователь ввёл не существующий ID пользователя");
            throw new NotFoundException("Пользователь c таким ID = " + id + " не найден!");
        }
    }

    private boolean userValidation(User user){
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Пользователь ввёл пустой или некорректный email");
            throw new NotFoundException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Пользователь ввёл пустой или некорректный login");
            throw new NotFoundException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пользователь ввёл пустое имя");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Пользователь ввёл некорректную дату рождения");
            throw new NotFoundException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}
