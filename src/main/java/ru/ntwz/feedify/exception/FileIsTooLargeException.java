package ru.ntwz.feedify.exception;

public class FileIsTooLargeException extends RuntimeException {
    public FileIsTooLargeException(String message) {
        super(message);
    }
}
