package com.openclassrooms.estate_back_end.exception;

import com.openclassrooms.estate_back_end.response.ErrorResponse;
import com.openclassrooms.estate_back_end.response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOGIN_URL = "/api/auth/login";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 bad request
            .body(new ErrorResponse("Validation failed"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageResponse> handleAuthenticationException(AuthenticationException exception,
            HttpServletRequest request) {
        if (request.getRequestURI().equals(LOGIN_URL)) {
            // message for failed login
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("error"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 unauthorized
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        // 400 bad request for unhandled exceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("An unexpected error occurred"));
    }

    @ExceptionHandler(RentalCreationException.class)
    public ResponseEntity<ErrorResponse> handleRentalCreationException(RentalCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RentalUpdateException.class)
    public ResponseEntity<ErrorResponse> handleRentalUpdateException(RentalUpdateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        // file size exceeded for picture import during rental creation
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("File size exceeds the maximum allowed size."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        // 400 message not found
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<MessageResponse> handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        // 401 unauthorized to send message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(exception.getMessage()));
    }

}
