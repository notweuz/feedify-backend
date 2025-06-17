package ru.ntwz.makemyfeed.exception;

public class FilesCannotBeEmptyException extends RuntimeException {
    public FilesCannotBeEmptyException(String message) {
        super(message);
    }
}
