package ru.ntwz.feedify.exception;

public class AttachmentNotFoundException extends RuntimeException {
  public AttachmentNotFoundException(String message) {
    super(message);
  }
} 