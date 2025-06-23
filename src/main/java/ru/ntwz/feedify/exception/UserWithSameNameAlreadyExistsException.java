package ru.ntwz.feedify.exception;

public class UserWithSameNameAlreadyExistsException extends RuntimeException {
    public UserWithSameNameAlreadyExistsException(String message) {
        super(message);
    }
}
