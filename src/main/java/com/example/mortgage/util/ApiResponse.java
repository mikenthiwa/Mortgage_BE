package com.example.mortgage.util;

import java.util.List;
import java.util.Optional;

public class ApiResponse {
    public boolean success;
    public int statusCode;
    public String message;
    public Object data;
    public List<String> errors;

    public static ApiResponse success(String message, int statusCode, Object data) {
        ApiResponse response = new ApiResponse();
        response.success = true;
        response.statusCode = statusCode;
        response.message = message;
        response.data = data;
        return response;
    }

    public static ApiResponse error(String message, int statusCode, List<String> errors) {
        ApiResponse response = new ApiResponse();
        response.success = false;
        response.statusCode = statusCode;
        response.message = message;
        response.errors = errors;
        return response;
    }

    // Getters and Setters
}
