package com.java.backend_test.producto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductoRequest(
        @NotBlank(message = "El nombre del producto no puede estar en blanco")
        String nombre,

        @NotNull(message = "El precio no puede ser nulo")
        @Positive(message = "El precio debe ser un número positivo")
        Double precio
) {
}
