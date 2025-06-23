package ru.ntwz.feedify.exception;

public class PostAlreadyDeletedException extends RuntimeException {
    public PostAlreadyDeletedException(String message) {
        super(message);
    }
}
