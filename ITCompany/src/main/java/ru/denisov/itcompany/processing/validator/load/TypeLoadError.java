package ru.denisov.itcompany.processing.validator.load;

public enum TypeLoadError {
    NON_UNIQUE("Non-unique value"),
    EMPTY("Empty value"),
    INCORRECT("Incorrect value");

    private final String description;

    TypeLoadError(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
