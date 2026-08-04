package com.jpau.challenge.infrastructure.adapter.in.rest.exception;

import com.jpau.challenge.application.exception.PriceNotFoundException;
import com.jpau.challenge.generated.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    ResponseEntity<ErrorResponse> handlePriceNotFound(PriceNotFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "PRICE_NOT_FOUND", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<ErrorResponse> handleInvalidDate(DateTimeException exception, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_APPLICATION_DATE",
                "The date parameter must use the format yyyy-MM-dd-HH.mm.ss",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<ErrorResponse> handleMethodValidation(HandlerMethodValidationException exception, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_REQUEST",
                "One or more request parameters are missing or invalid",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException exception, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "MISSING_REQUIRED_PARAMETER",
                "The query parameter '%s' is required".formatted(exception.getParameterName()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterType(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_PARAMETER",
                "The query parameter '%s' has an invalid value".formatted(exception.getName()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String code, String message, String path) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code);
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        errorResponse.setPath(path);
        return ResponseEntity.status(status).body(errorResponse);
    }

}