package ru.ntwz.makemyfeed.exception;

public class PostHasNoContentAtAllException extends RuntimeException {
    public PostHasNoContentAtAllException(String message) {
        super(message);
    }
}
