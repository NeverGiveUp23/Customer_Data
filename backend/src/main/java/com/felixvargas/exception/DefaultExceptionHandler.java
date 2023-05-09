package com.felixvargas.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice // This annotation will allow us to handle exceptions globally & multiple exceptions
public class DefaultExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(
            ResourceNotFoundException error,
            HttpServletRequest request,
            HttpServletResponse response){

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                error.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleException(
            DuplicateResourceException error,
            HttpServletRequest request,
            HttpServletResponse response){

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                error.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiError> handleException(
            RequestValidationException error,
            HttpServletRequest request,
            HttpServletResponse response){

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                error.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handleException(
            InsufficientAuthenticationException error,
            HttpServletRequest request,
            HttpServletResponse response){

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                error.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(
            BadCredentialsException error,
            HttpServletRequest request,
            HttpServletResponse response){

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                error.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }


}
