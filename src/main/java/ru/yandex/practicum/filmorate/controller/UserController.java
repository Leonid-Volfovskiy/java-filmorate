package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    protected void clearUsers() {
        userService.deleteAllUsers();
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        return userService.update(user);
    }

    //GET .../users/{id}
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }
    //PUT /users/{id}/friends/{friendId}
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userService.addFriend(id, friendId);
    }
    //DELETE /users/{id}/friends/{friendId}
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userService.deleteFriend(id, friendId);
    }
    //GET /users/{id}/friends
    @GetMapping("/{id}/friends")
    public List<User> listOfFriendsByID(@PathVariable("id") int id) {
        log.info("Запрошен список друзей Пользователя id = " + id); // вынести в контроллер
        return userService.listOfFriendsByID(id);
    }
    //GET /users/{id}/friends/common/{otherId}
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> listOfCommonFriends (@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("Запрошен список общих друзей для Пользователей: id = " + id + " и id = " + otherId);
        return userService.listOfCommonFriends(id, otherId);
    }

}