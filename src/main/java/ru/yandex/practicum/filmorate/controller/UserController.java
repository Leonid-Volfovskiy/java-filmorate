package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;
    private int newUserId() {
        return ++userId;
    }
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    protected void clearUsers() {
        users.clear();
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        User newUser = null;
        if (userValidation(user)) {
            newUser = User.builder()
                    .id(newUserId())
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .name(user.getName())
                    .birthday(user.getBirthday())
                    .build();
            users.put(newUser.getId(), newUser);
        }
        log.debug("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        User updatedUser = null;
        if (userValidation(user) && users.containsKey(user.getId())) {
            updatedUser = users.get(user.getId());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setName(user.getName());
            updatedUser.setBirthday(user.getBirthday());
            log.debug("Пользователь обновлен: {}", updatedUser);
        } else {
            throw new ValidationException("Пользователя с таким Id = " + user.getId() + " нет.");
        }
        return updatedUser;
    }

    private boolean userValidation(User user) throws ValidationException {
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Пользователь ввёл пустой или некорректный email при создании User.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Пользователь ввёл пустой или некорректный login при создании User.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пользователь ввёл пустое имя при создании User.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Пользователь ввёл некорректную дату рождения при создании User.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        return true;
    }
}