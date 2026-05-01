package com.opspilot.common.exception;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ErrorResponse.of("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("RESOURCE_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(UnsupportedIntentException.class)
    ResponseEntity<ErrorResponse> handleUnsupportedIntent(UnsupportedIntentException exception) {
        return ResponseEntity.unprocessableEntity()
                .body(ErrorResponse.of("UNSUPPORTED_INTENT", exception.getMessage()));
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidStateTransitionException exception) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("INVALID_STATE_TRANSITION", exception.getMessage()));
    }
}

