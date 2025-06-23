package ru.ntwz.feedify.exception;

public class AlreadyVotedForPostException extends RuntimeException {
    public AlreadyVotedForPostException(String message) {
        super(message);
    }
}
