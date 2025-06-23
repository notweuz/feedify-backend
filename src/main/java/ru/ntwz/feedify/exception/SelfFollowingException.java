package ru.ntwz.feedify.exception;

public class SelfFollowingException extends RuntimeException {
    public SelfFollowingException(String message) {
        super(message);
    }
} 