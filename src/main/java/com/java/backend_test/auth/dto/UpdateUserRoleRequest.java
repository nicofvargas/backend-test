package com.java.backend_test.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRoleRequest(
        @NotBlank String newRole
) {
}
