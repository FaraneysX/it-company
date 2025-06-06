package ru.denisov.itcompany.processing.check;

public class SurnameCheck {
    private static final String SURNAME_PATTERN = "^[A-Za-zА-Яа-яЁё\\-\\s']{1,50}$";

    public boolean isCorrect(String name) {
        return name.matches(SURNAME_PATTERN);
    }
}
