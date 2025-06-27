package com.java.backend_test.producto.controller;

import com.java.backend_test.producto.dto.ProductoRequest;
import com.java.backend_test.producto.dto.ProductoResponse;
import com.java.backend_test.producto.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    // Inyectamos el servicio
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponse>> obtenerTodos(Pageable pageable) {
        Page<ProductoResponse> productosPage = productoService.obtenerTodosLosProductos(pageable);
        return ResponseEntity.ok(productosPage);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest productoRequest) { // <-- Cambia el tipo de retorno
        ProductoResponse nuevoProducto = productoService.guardarProducto(productoRequest);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @PathVariable Long id, @Valid
            @RequestBody ProductoRequest productoRequest) {

        ProductoResponse productoActualizado = productoService.actualizarProducto(id, productoRequest);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}