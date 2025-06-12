package ru.ntwz.makemyfeed.exception;

public class NotSubscribedException extends RuntimeException {
    public NotSubscribedException(String message) {
        super(message);
    }
} 