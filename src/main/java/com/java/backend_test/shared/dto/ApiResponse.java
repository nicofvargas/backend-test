package com.java.backend_test.shared.dto;

public record ApiResponse(
        boolean success,
        String message
) {}