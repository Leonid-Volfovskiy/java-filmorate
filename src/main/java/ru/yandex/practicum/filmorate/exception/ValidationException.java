package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Exception {
    public ValidationException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}