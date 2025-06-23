package ru.ntwz.feedify.exception;

public class UnknownRatingValueException extends RuntimeException {
    public UnknownRatingValueException(String message) {
        super(message);
    }
}
