package ru.ntwz.makemyfeed.exception;

public class NotAuthorizedException extends RuntimeException {
  public NotAuthorizedException(String message) {
    super(message);
  }
}
