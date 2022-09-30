package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;

    protected void deleteAllFilms() {
        filmService.deleteAllFilms();
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") int filmId) {
        return filmService.getFilmById(filmId);
    }

    //PUT /films/{id}/like/{userId}
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId}
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.deleteLike(id, userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @Positive @RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    @ExceptionHandler
    public ResponseEntity< String > exc(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

