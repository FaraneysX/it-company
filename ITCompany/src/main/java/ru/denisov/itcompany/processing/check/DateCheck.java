package ru.denisov.itcompany.processing.check;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateCheck {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public boolean isCorrect(String date) {
        try {
            LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}
