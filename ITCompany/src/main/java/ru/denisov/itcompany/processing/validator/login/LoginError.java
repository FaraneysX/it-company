package ru.denisov.itcompany.processing.validator.login;

public enum LoginError {
    USER_NOT_FOUND("Пользователь с такой почтой и/или паролем не найден.");

    private final String description;

    LoginError(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
