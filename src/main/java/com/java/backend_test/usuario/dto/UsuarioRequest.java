package com.java.backend_test.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8, message = "La contraseña debe tener 8 caracteres min.") String password,
        @NotBlank String role
) {
}
