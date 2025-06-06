package ru.denisov.itcompany.processing.check;

public class NameCheck {
    private static final String NAME_PATTERN = "^[A-Za-zА-Яа-яЁё\\-\\s']{1,50}$";

    public boolean isCorrect(String name) {
        return name.matches(NAME_PATTERN);
    }
}
