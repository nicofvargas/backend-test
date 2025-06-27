package com.java.backend_test.controller;

import com.java.backend_test.entity.Producto;
import com.java.backend_test.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Producto> obtenerTodos() {
        // El controlador llama al servicio. No sabe de dónde vienen los datos.
        return productoService.obtenerTodosLosProductos();
    }
}