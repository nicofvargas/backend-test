package com.java.backend_test.usuario.dto;

import com.java.backend_test.usuario.EstadoUsuario;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "El nuevo estado de cuenta no puede ser nulo.")
        EstadoUsuario newState
) {
}
