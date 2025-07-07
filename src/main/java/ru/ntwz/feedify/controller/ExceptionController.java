package ru.ntwz.feedify.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ntwz.feedify.dto.response.ApiError;
import ru.ntwz.feedify.exception.*;

import java.util.Collections;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleInvalidPasswordException(InvalidPasswordException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.UNAUTHORIZED.name());
        apiError.setReason("Provided password is invalid");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ApiError> handleNotAuthorizedException(NotAuthorizedException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.UNAUTHORIZED.name());
        apiError.setReason("User is not authorized");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("User not found");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithSameNameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserWithSameNameAlreadyExistsException(UserWithSameNameAlreadyExistsException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.CONFLICT.name());
        apiError.setReason("User with the same name already exists");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiError> handlePostNotFoundException(PostNotFoundException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("Post not found");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotProvidedException.class)
    public ResponseEntity<ApiError> handleTokenNotProvidedException(TokenNotProvidedException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.UNAUTHORIZED.name());
        apiError.setReason("Access token is not provided");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotPostsOwnerException.class)
    public ResponseEntity<ApiError> handleNotPostsOwnerException(NotPostsOwnerException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.FORBIDDEN.name());
        apiError.setReason("User is not the owner of the post");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PostAlreadyDeletedException.class)
    public ResponseEntity<ApiError> handlePostAlreadyDeletedException(PostAlreadyDeletedException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("Post has already been deleted");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnknownRatingValueException.class)
    public ResponseEntity<ApiError> handleUnknownRatingValueException(UnknownRatingValueException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Unknown rating value provided");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyVotedForPostException.class)
    public ResponseEntity<ApiError> handleAlreadyVotedForPostException(AlreadyVotedForPostException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.CONFLICT.name());
        apiError.setReason("User has already voted for this post");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Invalid argument provided");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
        apiError.setReason("An unexpected error occurred");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SelfFollowingException.class)
    public ResponseEntity<ApiError> handleSelfSubscriptionException(SelfFollowingException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("User cannot follow themselves");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<ApiError> handleAlreadySubscribedException(AlreadyFollowingException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.CONFLICT.name());
        apiError.setReason("User is already following this account");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<ApiError> handleNotSubscribedException(NotFollowingException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("User is not following this account");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleFileNotFoundException(FileNotFoundException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("File not found");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileReadingException.class)
    public ResponseEntity<ApiError> handleFileReadingException(FileReadingException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
        apiError.setReason("Error reading file");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<ApiError> handleFileIsEmptyException(FileIsEmptyException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("File is empty");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileIsTooLargeException.class)
    public ResponseEntity<ApiError> handleFileIsTooLargeException(FileIsTooLargeException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.name());
        apiError.setReason("File is too large");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(TooManyAttachmentsException.class)
    public ResponseEntity<ApiError> handleTooManyAttachmentsException(TooManyAttachmentsException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Too many attachments");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<ApiError> handleAttachmentNotFoundException(AttachmentNotFoundException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("Attachment not found");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostHasNoContentAtAllException.class)
    public ResponseEntity<ApiError> handlePostHasNoContentAtAllException(PostHasNoContentAtAllException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Post content cannot be empty");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FilesCannotBeEmptyException.class)
    public ResponseEntity<ApiError> handleFilesCannotBeEmptyException(FilesCannotBeEmptyException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Files cannot be empty");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("One or more validation constraints were violated");
        apiError.setErrors(Collections.singletonList(ex.toString()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Validation failed for one or more fields");
        apiError.setErrors(Collections.singletonList(ex.toString()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
