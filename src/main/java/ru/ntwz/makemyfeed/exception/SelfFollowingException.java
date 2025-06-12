package ru.ntwz.makemyfeed.exception;

public class SelfFollowingException extends RuntimeException {
    public SelfFollowingException(String message) {
        super(message);
    }
} 