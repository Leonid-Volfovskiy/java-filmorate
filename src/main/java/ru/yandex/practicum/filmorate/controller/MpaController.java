package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAll() {
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        return mpaService.getById(id);
    }
}