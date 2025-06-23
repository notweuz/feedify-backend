package ru.ntwz.feedify.exception;

public class TooManyAttachmentsException extends RuntimeException {
    public TooManyAttachmentsException(String message) {
        super(message);
    }
} 