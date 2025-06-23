package ru.ntwz.feedify.exception;

public class FilesCannotBeEmptyException extends RuntimeException {
    public FilesCannotBeEmptyException(String message) {
        super(message);
    }
}
