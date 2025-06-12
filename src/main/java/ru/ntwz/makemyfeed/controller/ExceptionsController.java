package ru.ntwz.makemyfeed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ntwz.makemyfeed.exception.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Map<String, String>> handleNotAuthorizedException(NotAuthorizedException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithSameNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserWithSameNameAlreadyExistsException(UserWithSameNameAlreadyExistsException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePostNotFoundException(PostNotFoundException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotProvidedException.class)
    public ResponseEntity<Map<String, String>> handleTokenNotProvidedException(TokenNotProvidedException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotPostsOwnerException.class)
    public ResponseEntity<Map<String, String>> handleNotPostsOwnerException(NotPostsOwnerException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PostAlreadyDeletedException.class)
    public ResponseEntity<Map<String, String>> handlePostAlreadyDeletedException(PostAlreadyDeletedException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownRatingValueException.class)
    public ResponseEntity<Map<String, String>> handleUnknownRatingValueException(UnknownRatingValueException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyVotedForPostException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyVotedForPostException(AlreadyVotedForPostException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SelfSubscriptionException.class)
    public ResponseEntity<Map<String, String>> handleSelfSubscriptionException(SelfSubscriptionException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadySubscribedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadySubscribedException(AlreadySubscribedException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotSubscribedException.class)
    public ResponseEntity<Map<String, String>> handleNotSubscribedException(NotSubscribedException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
}
