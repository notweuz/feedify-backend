package ru.ntwz.makemyfeed.exception;

public class TooManyAttachmentsException extends RuntimeException {
    public TooManyAttachmentsException(String message) {
        super(message);
    }
} 