package com.ironhack.carrentalsystem.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle invalid parameters
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String,String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        // Only handle conversion errors on ID parameters
        if ("id".equals(ex.getName()) && ex.getRequiredType() == Long.class) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "'" + ex.getValue() + "' is not a valid ID number"));
        }
        // Fallback to 400 with generic message
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid parameter '" + ex.getName() + "': " + ex.getValue()));
    }

    // Handle LocalDateTime errors and non-existing enums errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();
            // Handle LocalDateTime format errors
            if (targetType.equals(java.time.LocalDateTime.class)) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid date-time format. Please use: yyyy-MM-ddTHH:mm:ss")
                );
            }
            // Handle non-existing enum format errors
            if (targetType.isEnum()) {
                Object value = ife.getValue();
                String allowedValues = Arrays.toString(targetType.getEnumConstants());
                return ResponseEntity.badRequest().body(
                        Map.of("error", "\"" + value + "\" is not a valid value. Allowed values: " + allowedValues)
                );
            }
        }
        // Fallback generic error
        return ResponseEntity.badRequest().body(
                Map.of("error", "Malformed JSON request: " + ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
