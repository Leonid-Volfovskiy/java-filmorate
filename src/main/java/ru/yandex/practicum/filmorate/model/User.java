package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @Email
    @NotNull
    private String email;
    private String name;
    @NotBlank
    private String login;
    @Past
    @NotNull
    private LocalDate birthday;
    private Set<Integer> friends;
}

/*
целочисленный идентификатор — id;
электронная почта — email;
логин пользователя — login;
имя для отображения — name;
дата рождения — birthday.
 */