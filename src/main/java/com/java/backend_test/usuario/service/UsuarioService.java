package com.java.backend_test.usuario.service;

import com.java.backend_test.shared.exception.ResourceAlreadyExistsException;
import com.java.backend_test.shared.exception.ResourceNotFoundException;
import com.java.backend_test.usuario.EstadoUsuario;
import com.java.backend_test.usuario.Usuario;
import com.java.backend_test.usuario.dto.UsuarioRequest;
import com.java.backend_test.usuario.dto.UsuarioResponse;
import com.java.backend_test.usuario.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        usuarioRepository.findByUsername(usuarioRequest.username()).ifPresent(u -> {
            throw new ResourceAlreadyExistsException("El nombre de usuario '" + usuarioRequest.username() + "' ya está en uso.");
        });

        usuarioRepository.findByEmail(usuarioRequest.email()).ifPresent(u -> {
            throw new ResourceAlreadyExistsException("El email '" + usuarioRequest.email() + "' ya está en uso.");
        });

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuarioRequest.username());
        nuevoUsuario.setPassword(passwordEncoder.encode(usuarioRequest.password()));
        nuevoUsuario.setRole(usuarioRequest.role());
        nuevoUsuario.setEmail(usuarioRequest.email());
        // --- NUEVA LÓGICA DE VERIFICACIÓN ---
        nuevoUsuario.setEstado(EstadoUsuario.PENDIENTE_VERIFICACION);
        String token = UUID.randomUUID().toString();
        nuevoUsuario.setVerificationToken(token);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // --- SIMULACIÓN DE ENVÍO DE EMAIL ---
        System.out.println("----------------------------------------------------");
        System.out.println("SIMULACIÓN DE ENVÍO DE EMAIL:");
        System.out.println("Para: " + usuarioGuardado.getUsername());
        System.out.println("Asunto: Verifique su cuenta");
        System.out.println("Token de verificación: " + token);
        System.out.println("Enlace de verificación (para usar en Postman):");
        System.out.println("GET http://localhost:8080/api/auth/verify?token=" + token);
        System.out.println("----------------------------------------------------");


        return new UsuarioResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getUsername(),
                usuarioGuardado.getRole()
        );
    }


    //devuelve todos los usuarios solo puede usarlo el admin o quien tenga permisos
    public Page<UsuarioResponse> obtenerTodosLosUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuario -> new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole()
        ));
    }
    //obtiene por id
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole()
        );
    }
    //elimina por id
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    //devuelve un UsuarioResponse del usuario
    public UsuarioResponse obtenerUsuarioPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole()
        );
    }

    //cambia contraseña
    public void cambiarPassword(String username, String oldPassword, String newPassword) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 1. Verificar que la contraseña antigua es correcta
        if (!passwordEncoder.matches(oldPassword, usuario.getPassword())) {
            throw new IllegalStateException("La contraseña antigua es incorrecta"); // Podríamos crear una excepción personalizada
        }

        // 2. Cifrar y guardar la nueva contraseña
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    //actualiza rol
    public UsuarioResponse actualizarRol(Long id, String newRole) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        usuario.setRole(newRole);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getId(),
                usuarioActualizado.getUsername(),
                usuarioActualizado.getRole()
        );
    }

    @Transactional // Importante para asegurar que todas las operaciones de BD se completen o ninguna
    public void verificarUsuario(String token) {
        // 1. Buscar al usuario por el token de verificación
        Usuario usuario = usuarioRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de verificación inválido o expirado."));

        // 2. Cambiar el estado del usuario a ACTIVO
        usuario.setEstado(EstadoUsuario.ACTIVO);

        // 3. Anular el token para que no pueda ser reutilizado
        usuario.setVerificationToken(null);

        // 4. Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);
    }

    public void generatePasswordResetToken(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro un usuario con el mail: " + email));

        String token = UUID.randomUUID().toString();
        usuario.setPasswordResetToken(token);
        //valido por 15min
        usuario.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);
        //simulacion de envio mail
        System.out.println("----------------------------------------------------");
        System.out.println("SIMULACIÓN DE EMAIL DE RESET DE CONTRASEÑA:");
        System.out.println("Para: " + usuario.getEmail());
        System.out.println("Token de reseteo: " + token);
        System.out.println("Este token expira en 15 minutos.");
        System.out.println("----------------------------------------------------");
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        Usuario usuario = usuarioRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de reseteo inválido."));

        // verifica token expirado
        if (usuario.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El token de reseteo ha expirado.");
        }

        // actualiza la contraseña
        usuario.setPassword(passwordEncoder.encode(newPassword));

        // anula el token
        usuario.setPasswordResetToken(null);
        usuario.setPasswordResetTokenExpiry(null);

        usuarioRepository.save(usuario);
    }
    //este metodo es parecido al de rol pero este es para funcionalidades a futuro de tipo de usuario ejemplo: bloqueado, suspendido, vip, etc.
    public UsuarioResponse actualizarEstado(Long id, EstadoUsuario newStatus) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        usuario.setEstado(newStatus);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getId(),
                usuarioActualizado.getUsername(),
                usuarioActualizado.getRole()
        );
    }
}
