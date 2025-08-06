package com.example.mortgage.model;

import java.util.List;
import java.util.Optional;

public class ApiResponse {
    public boolean success;
    public int statusCode;
    public String message;
    public Optional<Object> data;
    public Optional<List<String>> errors;

    public static ApiResponse success(String message, int statusCode, Object data) {
        ApiResponse response = new ApiResponse();
        response.success = true;
        response.statusCode = statusCode;
        response.message = message;
        response.data = Optional.ofNullable(data);
        return response;
    }

    public static ApiResponse error(String message, int statusCode, List<String> errors) {
        ApiResponse response = new ApiResponse();
        response.success = false;
        response.statusCode = statusCode;
        response.message = message;
        response.errors = Optional.ofNullable(errors);
        return response;
    }


}
