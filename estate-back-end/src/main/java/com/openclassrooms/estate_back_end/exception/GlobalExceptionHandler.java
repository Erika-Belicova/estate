package com.openclassrooms.estate_back_end.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOGIN_URL = "/api/auth/login";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().build(); // 400 bad request
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, HttpServletRequest request) {
        if (request.getRequestURI().equals(LOGIN_URL)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "error")); // error message for failed login
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 unauthorized
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGeneralException(Exception exception) {
        return ResponseEntity.badRequest().build(); // 400 bad request for unhandled exceptions
    }
}
