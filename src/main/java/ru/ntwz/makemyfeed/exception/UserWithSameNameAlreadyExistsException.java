package ru.ntwz.makemyfeed.exception;

public class UserWithSameNameAlreadyExistsException extends RuntimeException {
    public UserWithSameNameAlreadyExistsException(String message) {
        super(message);
    }
}
