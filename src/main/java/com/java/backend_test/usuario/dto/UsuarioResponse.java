package com.java.backend_test.usuario.dto;

public record UsuarioResponse(
        Long id,
        String username,
        String role
) {
}
