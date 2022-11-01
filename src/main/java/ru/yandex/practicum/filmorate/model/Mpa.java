package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    private int raitingId;
    @NotBlank
    private String mpaName;
}
