package com.example.mortgage.exception;

import com.example.mortgage.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        ApiResponse response = ApiResponse.error("An unexpected error occurred", 500, List.of(ex.getMessage()));
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        ApiResponse response = ApiResponse.error("The specified resource was not found", 404, List.of(ex.getMessage()));
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse> handleInvalidInputException(InvalidInputException ex) {
        ApiResponse response = ApiResponse.error(ex.getMessage(), 400, List.of(ex.getMessage()));
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse response = ApiResponse.error(ex.getMessage(), 404, List.of(ex.getMessage()));
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ApiResponse response = ApiResponse.error("Unauthorized access", 401, List.of(ex.getMessage()));
        return ResponseEntity.status(401).body(response);
    }
}
