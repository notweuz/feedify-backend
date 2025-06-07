package ru.ntwz.makemyfeed.exception;

public class PostAlreadyDeletedException extends RuntimeException {
    public PostAlreadyDeletedException(String message) {
        super(message);
    }
}
