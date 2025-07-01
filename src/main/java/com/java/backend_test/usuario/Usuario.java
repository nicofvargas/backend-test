package com.java.backend_test.usuario;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) // Le dice a JPA que guarde el nombre del Enum ("ACTIVO") en lugar de su número (1)
    @Column(nullable = false)
    private EstadoUsuario estado;
    private String verificationToken; // Este puede ser nulo una vez que la cuenta está activa
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;
}
