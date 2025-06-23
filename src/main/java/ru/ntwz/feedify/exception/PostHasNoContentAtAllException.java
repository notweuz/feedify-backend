package ru.ntwz.feedify.exception;

public class PostHasNoContentAtAllException extends RuntimeException {
    public PostHasNoContentAtAllException(String message) {
        super(message);
    }
}
