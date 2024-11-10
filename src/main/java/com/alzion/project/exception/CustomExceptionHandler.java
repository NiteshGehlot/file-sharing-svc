package com.alzion.project.exception;

import com.alzion.project.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InvalidPassCodeException.class)
    public ResponseEntity<?> handleInvalidPassCodeException(InvalidPassCodeException e) {
        ErrorResponse errorResponse = new ErrorResponse("Failed", "AL-001", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("Failed", "AL-002", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IncorrectFileSizeException.class)
    public ResponseEntity<?> handleIncorrectFileSizeException(IncorrectFileSizeException e) {
        ErrorResponse errorResponse = new ErrorResponse("Failed", "AL-003", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
