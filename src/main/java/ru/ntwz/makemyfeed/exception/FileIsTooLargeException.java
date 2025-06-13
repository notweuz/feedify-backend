package ru.ntwz.makemyfeed.exception;

public class FileIsTooLargeException extends RuntimeException {
    public FileIsTooLargeException(String message) {
        super(message);
    }
}
