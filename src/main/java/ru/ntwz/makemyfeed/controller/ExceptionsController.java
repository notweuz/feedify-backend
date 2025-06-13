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

    @ExceptionHandler(SelfFollowingException.class)
    public ResponseEntity<Map<String, String>> handleSelfSubscriptionException(SelfFollowingException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<Map<String, String>> handleAlreadySubscribedException(AlreadyFollowingException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<Map<String, String>> handleNotSubscribedException(NotFollowingException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFileNotFoundException(FileNotFoundException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileReadingException.class)
    public ResponseEntity<Map<String, String>> handleFileReadingException(FileReadingException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<Map<String, String>> handleFileIsEmptyException(FileIsEmptyException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileIsTooLargeException.class)
    public ResponseEntity<Map<String, String>> handleFileIsTooLargeException(FileIsTooLargeException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
