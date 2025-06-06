package ru.denisov.itcompany.processing.check;

public class EmailCheck {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public boolean isCorrect(String email) {
        return email.matches(EMAIL_PATTERN);
    }
}
