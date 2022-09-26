package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
/*    static InMemoryFilmStorage inMemoryFilmStorage;
    static FilmService filmService;
    static FilmController filmController;


    @BeforeAll
    static void setUp(){
        inMemoryFilmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(inMemoryFilmStorage);
        filmController = new FilmController(inMemoryFilmStorage, filmService);

        Film film1 = Film.builder()
                .id(0)
                .name("Gone in Sixty Seconds")
                .description("Классное кино про угон авто")
                .releaseDate(LocalDate.of(2000, 6, 15))
                .duration(118)
                .build();

        Film film2 = Film.builder()
                .id(0)
                .name("The Avengers")
                .description("Команда супергероев дает отпор скандинавскому богу Локи.")
                .releaseDate(LocalDate.of(2012, 5, 3))
                .duration(137)
                .build();

        Film film3 = Film.builder()
                .id(0)
                .name("Harry Potter and the Sorcerer's Stone")
                .description("Первая часть большой франшизы о маленьком волшебнике")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(152)
                .build();

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);
    }

    @AfterAll
    static void tearDown() {
        filmController.deleteAllFilms();
    }

    @Test
    void checkFilmStorage() {
        assertEquals(3, filmController.findAll().size(), "Список фильмов не корректный");
    }

    @Test
    void addFilmWithEmptyName() {
        Film film4 = Film.builder()
                .id(0)
                .name(" ")
                .description("Гарри Поттер переходит на второй курс Школы чародейства и волшебства Хогвартс.")
                .releaseDate(LocalDate.of(2002, 11, 10))
                .duration(161)
                .build();
        try {
            filmController.create(film4);
        } catch (NotFoundException thrown) {
            assertEquals("Название не может быть пустым", thrown.getMessage());
        }
    }

    @Test
    void addFilmWithNullName() {
        Film film4 = Film.builder()
                .id(0)
                .name(null)
                .description("Гарри Поттер переходит на второй курс Школы чародейства и волшебства Хогвартс.")
                .releaseDate(LocalDate.of(2002, 11, 10))
                .duration(161)
                .build();
        try {
            filmController.create(film4);
        } catch (NotFoundException thrown) {
            assertEquals("Название не может быть пустым", thrown.getMessage());
        }
    }

    @Test
    void addFilmWithTooLongDescription() {
        Film film4 = Film.builder()
                .id(0)
                .name("Harry Potter and the Chamber of Secrets")
                .description("Гарри Поттер переходит на второй курс Школы чародейства и волшебства Хогвартс. " +
                        "Эльф Добби предупреждает Гарри об опасности, которая поджидает его там" +
                        ", и просит больше не возвращаться в школу. Юный волшебник не следует совету эльфа " +
                        "и становится свидетелем таинственных событий, разворачивающихся в Хогвартсе. " +
                        "Вскоре Гарри и его друзья узнают о существовании Тайной Комнаты и сталкиваются " +
                        "с новыми приключениями, пытаясь победить темные силы.")
                .releaseDate(LocalDate.of(2002, 11, 10))
                .duration(161)
                .build();
        try {
            filmController.create(film4);
        } catch (NotFoundException thrown) {
            assertEquals("Максимальная длина описания — 200 символов", thrown.getMessage());
        }
    }

    @Test
    void addFilmWithWrongReleaseDate() {
        Film film4 = Film.builder()
                .id(0)
                .name("Harry Potter and the Chamber of Secrets")
                .description("Гарри Поттер переходит на второй курс Школы чародейства и волшебства Хогвартс.")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(161)
                .build();
        try {
            filmController.create(film4);
        } catch (NotFoundException thrown) {
            assertEquals("Дата релиза — не раньше 28 декабря 1895 года", thrown.getMessage());
        }
    }

    @Test
    void addFilmWithNegativeFilmDuration() {
        Film film4 = Film.builder()
                .id(0)
                .name("Harry Potter and the Chamber of Secrets")
                .description("Гарри Поттер переходит на второй курс Школы чародейства и волшебства Хогвартс.")
                .releaseDate(LocalDate.of(2002, 11, 10))
                .duration(-161)
                .build();
        try {
            filmController.create(film4);
        } catch (NotFoundException thrown) {
            assertEquals("Продолжительность фильма должна быть положительной", thrown.getMessage());
        }
    }

    @Test
    void updateFilm(){

        Film filmForUpdate = Film.builder()
                .id(3)
                .name("Harry Potter and the Sorcerer's Stone - 1 part")
                .description("Первая часть большой франшизы о маленьком волшебнике Гарри")
                .releaseDate(LocalDate.of(2001, 11, 3))
                .duration(150)
                .build();

        filmController.put(filmForUpdate);

        Film updatedFilm3 = null;
        Collection<Film> filmList = filmController.findAll();
        for (Film film: filmList) {
            if(film.getId() == 3) {
                updatedFilm3 = film;
            }
        }

        assert updatedFilm3 != null;
        assertEquals(updatedFilm3.getName(), filmForUpdate.getName());
        assertEquals(updatedFilm3.getDescription(), filmForUpdate.getDescription());
        assertEquals(updatedFilm3.getReleaseDate(), filmForUpdate.getReleaseDate());
        assertEquals(updatedFilm3.getDuration(), filmForUpdate.getDuration());
    }

    @Test
    void updateFilmWithWrongId() {
        Film filmForUpdate = Film.builder()
                .id(4)
                .name("Harry Potter and the Sorcerer's Stone - 1 part")
                .description("Первая часть большой франшизы о маленьком волшебнике Гарри")
                .releaseDate(LocalDate.of(2001, 11, 3))
                .duration(150)
                .build();

        try {
            filmController.put(filmForUpdate);
        } catch (NotFoundException thrown) {
            assertEquals("Фильма с таким Id = 4 нет", thrown.getMessage());
        }

    }*/

}