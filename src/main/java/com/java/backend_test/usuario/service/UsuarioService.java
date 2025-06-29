package com.java.backend_test.usuario.service;

import com.java.backend_test.shared.exception.ResourceNotFoundException;
import com.java.backend_test.usuario.Usuario;
import com.java.backend_test.usuario.dto.UsuarioRequest;
import com.java.backend_test.usuario.dto.UsuarioResponse;
import com.java.backend_test.usuario.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        //validacion
        usuarioRepository.findByUsername(usuarioRequest.username()).ifPresent(u -> {
            throw new IllegalStateException("El nombre de usuario ya está en uso"); // Mejoraremos esto luego
        });
        ///////////////////////////////////
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuarioRequest.username());
        nuevoUsuario.setPassword(passwordEncoder.encode(usuarioRequest.password()));
        nuevoUsuario.setRole(usuarioRequest.role());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

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
}
