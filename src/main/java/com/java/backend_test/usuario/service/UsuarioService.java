package com.java.backend_test.usuario.service;

import com.java.backend_test.usuario.Usuario;
import com.java.backend_test.usuario.dto.UsuarioRequest;
import com.java.backend_test.usuario.dto.UsuarioResponse;
import com.java.backend_test.usuario.repository.UsuarioRepository;
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
        //falta agregar validacion si el usernmae existe

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
}
