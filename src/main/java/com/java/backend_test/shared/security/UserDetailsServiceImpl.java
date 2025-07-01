package com.java.backend_test.shared.security;

import com.java.backend_test.usuario.EstadoUsuario;
import com.java.backend_test.usuario.Usuario;
import com.java.backend_test.usuario.repository.UsuarioRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscar el usuario en nuestra base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // --- NUEVA COMPROBACIÓN DE ESTADO ---
        if (usuario.getEstado() == EstadoUsuario.PENDIENTE_VERIFICACION) {
            throw new DisabledException("La cuenta de usuario para '" + username + "' no está activa.");
        }

        if (usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new LockedException("La cuenta de usuario para '" + username + "' está bloqueada.");
        }

        // 2. Construir el objeto UserDetails que Spring Security necesita
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole()))
        );
    }
}
