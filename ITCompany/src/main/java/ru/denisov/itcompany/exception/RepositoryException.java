package ru.denisov.itcompany.exception;

public class RepositoryException extends RuntimeException {
    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message) {
        super(message);
    }
}
