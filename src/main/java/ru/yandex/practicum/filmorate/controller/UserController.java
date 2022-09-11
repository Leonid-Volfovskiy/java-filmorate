package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    protected void clearUsers() {
        users.clear();
    }

    private int newUserId() {
        return ++userId;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user){
        userValidation(user);
        user.setId(newUserId());
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user){
        User updatedUser = null;
        if (userValidation(user) && users.containsKey(user.getId())) {
            updatedUser = users.get(user.getId());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setName(user.getName());
            updatedUser.setBirthday(user.getBirthday());
            log.debug("Пользователь обновлен: {}", updatedUser);
        } else {
            throw new ValidationException("Пользователя с таким Id = " + user.getId() + " нет");
        }
        return updatedUser;
    }

    private boolean userValidation(User user){
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Пользователь ввёл пустой или некорректный email");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Пользователь ввёл пустой или некорректный login");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пользователь ввёл пустое имя");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Пользователь ввёл некорректную дату рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}