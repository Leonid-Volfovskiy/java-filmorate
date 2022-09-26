package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
/*    static InMemoryUserStorage inMemoryUserStorage;
    static UserService userService;
    static UserController userController;

    @BeforeAll
    static void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);

        User user1 = User.builder()
                .id(0)
                .email("user1@yandex.ru")
                .login("BadBoy")
                .name("Alex")
                .birthday(LocalDate.of(2000, 6, 14))
                .build();

        User user2 = User.builder()
                .id(0)
                .email("user2@yandex.ru")
                .login("Mr.Green")
                .name("John")
                .birthday(LocalDate.of(1996, 3, 25))
                .build();

        User user3 = User.builder()
                .id(0)
                .email("user3@yandex.ru")
                .login("Destroyer")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);
    }

    @AfterAll
    static void tearDown() {
        userController.clearUsers();
    }

    @Test
    void checkUsersStorage() {
        assertEquals(3, userController.findAll().size(), "Список пользователей не корректный");
    }

    @Test
    void addUserWithEmptyEmail() {
        User user4 = User.builder()
                .id(0)
                .email(" ")
                .login("Destroyer")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithNullEmail() {
        User user4 = User.builder()
                .id(0)
                .email(null)
                .login("Destroyer")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithOutATInEmail() {
        User user4 = User.builder()
                .id(0)
                .email("user4yandex.ru")
                .login("Destroyer")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithEmptyLogin() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login(" ")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Логин не может быть пустым и содержать пробелы"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithNullLogin() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login(null)
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Логин не может быть пустым и содержать пробелы"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithGapInLogin() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login("Dest royer")
                .name("Max")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();
        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Логин не может быть пустым и содержать пробелы"
                    , thrown.getMessage());
        }
    }

    @Test
    void addUserWithEmptyName() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login("Destroyer")
                .name(" ")
                .birthday(LocalDate.of(1982, 9, 1))
                .build();

        User newUser = userController.create(user4);
        assertEquals("Destroyer", newUser.getName(), "Имя не установлено");
    }

    @Test
    void addUserWithNullName() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login("Destroyer")
                .name(null)
                .birthday(LocalDate.of(1982, 9, 1))
                .build();

        User newUser = userController.create(user4);
        assertEquals("Destroyer", newUser.getName(), "Имя не установлено");
    }

    @Test
    void addUserWithWrongBirthday() {
        User user4 = User.builder()
                .id(0)
                .email("user4@yandex.ru")
                .login("Destroyer")
                .name("Anthony")
                .birthday(LocalDate.of(2024, 5, 26))
                .build();

        try {
            userController.create(user4);
        } catch (NotFoundException thrown) {
            assertEquals("Дата рождения не может быть в будущем", thrown.getMessage());
        }
    }

    @Test
    void updateUser() {
        User userForUpdate = User.builder()
                .id(3)
                .email("user4@yandex.ru")
                .login("Destroyer$")
                .name("Anthony")
                .birthday(LocalDate.of(1992, 5, 26))
                .build();

        userController.update(userForUpdate);

        User updatedUser3 = null;
        Collection<User> usersList = userController.findAll();
        for (User user: usersList) {
            if(user.getId() == 3) {
                updatedUser3 = user;
            }
        }

        assert updatedUser3 != null;
        if (updatedUser3.getEmail() != null) {
            assertEquals(updatedUser3.getEmail(), userForUpdate.getEmail());
        }
        assertEquals(updatedUser3.getLogin(), userForUpdate.getLogin());
        assertEquals(updatedUser3.getName(), userForUpdate.getName());
        assertEquals(updatedUser3.getBirthday(), userForUpdate.getBirthday());
    }

    @Test
    void updateUserWithWrongId() {
        User userForUpdate = User.builder()
                .id(4)
                .email("user4@yandex.ru")
                .login("Destroyer$")
                .name("Anthony")
                .birthday(LocalDate.of(1992, 5, 26))
                .build();

        try {
            userController.update(userForUpdate);
        } catch (NotFoundException thrown) {
            assertEquals("Пользователя с таким Id = 4 нет", thrown.getMessage());
        }

    }*/
}