package ru.ntwz.makemyfeed.exception;

public class AlreadyVotedForPostException extends RuntimeException {
    public AlreadyVotedForPostException(String message) {
        super(message);
    }
}
