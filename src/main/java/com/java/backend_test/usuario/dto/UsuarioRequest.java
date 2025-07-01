package com.java.backend_test.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record UsuarioRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8, message = "La contraseña debe tener 8 caracteres min.") String password,
        @NotBlank @Email(message = "El formato del email no es válido") String email,
        @NotBlank String role
) {
}
