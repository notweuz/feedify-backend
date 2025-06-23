package ru.ntwz.feedify.exception;

public class FileReadingException extends RuntimeException {
    public FileReadingException(String message) {
        super(message);
    }
}
