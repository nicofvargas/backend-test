package com.java.backend_test.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Anotación de Lombok: genera getters, setters, toString, etc.
@Entity // Le dice a JPA que esta clase es una tabla en la BD.
public class Producto {

    @Id // Marca este campo como la clave primaria (Primary Key).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Le dice a la BD que genere el ID automáticamente.
    private Long id;

    private String nombre;
    private Double precio;
}
