package com.java.backend_test.auth.controller;

import com.java.backend_test.auth.dto.ChangePasswordRequest;
import com.java.backend_test.auth.dto.LoginRequest;
import com.java.backend_test.auth.dto.LoginResponse;
import com.java.backend_test.shared.security.JwtProvider;
import com.java.backend_test.usuario.dto.UsuarioResponse;
import com.java.backend_test.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.usuarioService = usuarioService;
    }
    //POST
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Crear un objeto de autenticación con las credenciales del usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // 2. Si la autenticación es exitosa, establecerla en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT
        String token = jwtProvider.generateToken(authentication);

        // 4. Devolver el token en la respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }
    //GET
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getMiPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        // El UserDetails que nos da Spring Security contiene el username.
        String username = userDetails.getUsername();
        UsuarioResponse usuarioResponse = usuarioService.obtenerUsuarioPorUsername(username);
        return ResponseEntity.ok(usuarioResponse);
    }
    //PUT
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        usuarioService.cambiarPassword(
                userDetails.getUsername(),
                changePasswordRequest.oldPassword(),
                changePasswordRequest.newPassword()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUserAccount(@RequestParam("token") String token) {
        usuarioService.verificarUsuario(token);
        return ResponseEntity.ok("¡Cuenta verificada exitosamente! Ahora puedes iniciar sesión.");
    }
}