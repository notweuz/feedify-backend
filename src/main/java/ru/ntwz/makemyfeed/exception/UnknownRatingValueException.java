package ru.ntwz.makemyfeed.exception;

public class UnknownRatingValueException extends RuntimeException {
    public UnknownRatingValueException(String message) {
        super(message);
    }
}
