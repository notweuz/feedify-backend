package ru.ntwz.feedify.exception;

public class NotPostsOwnerException extends RuntimeException {
    public NotPostsOwnerException(String message) {
        super(message);
    }
}
