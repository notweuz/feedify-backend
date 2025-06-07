package ru.ntwz.makemyfeed.exception;

public class NotPostsOwnerException extends RuntimeException {
    public NotPostsOwnerException(String message) {
        super(message);
    }
}
