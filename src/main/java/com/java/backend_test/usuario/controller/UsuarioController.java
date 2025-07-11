package com.java.backend_test.usuario.controller;


import com.java.backend_test.auth.dto.UpdateUserRoleRequest;
import com.java.backend_test.usuario.dto.UpdateUserStatusRequest;
import com.java.backend_test.usuario.dto.UsuarioRequest;
import com.java.backend_test.usuario.dto.UsuarioResponse;
import com.java.backend_test.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse nuevoUsuario = usuarioService.crearUsuario(usuarioRequest);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> obtenerTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    //PUT
    @PutMapping("/{id}/role")
    public ResponseEntity<UsuarioResponse> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {

        UsuarioResponse updatedUser = usuarioService.actualizarRol(id, request.newRole());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UsuarioResponse> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        UsuarioResponse updatedUser = usuarioService.actualizarEstado(id, request.newState());
        return ResponseEntity.ok(updatedUser);
    }
}
