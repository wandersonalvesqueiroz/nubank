package com.nubank.capitalgain.exception;

import com.nubank.capitalgain.generated.model.ErrorResponseBase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponseBase> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus status;
        String error;
        String message;

        switch (ex) {
            case IllegalArgumentException illegalEx -> {
                status = HttpStatus.BAD_REQUEST;
                error = "Argumento inválido";
                message = illegalEx.getMessage();
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                error = "Erro interno";
                message = ex.getMessage();
            }
        }

        return buildErrorResponse(status, error, message, request.getRequestURI());
    }

    private ResponseEntity<ErrorResponseBase> buildErrorResponse(HttpStatus status, String error, String message, String path) {
        ErrorResponseBase response = new ErrorResponseBase();
        response.setTimestamp(OffsetDateTime.now());
        response.setStatus(status.value());
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);

        return ResponseEntity.status(status).body(response);
    }
}
