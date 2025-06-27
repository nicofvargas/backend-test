package com.java.backend_test.service;

import com.java.backend_test.entity.Producto;
import com.java.backend_test.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Le dice a Spring que esta clase es un componente de servicio.
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección de dependencias por constructor (la mejor práctica).
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodosLosProductos() {
        // El servicio usa el repositorio para obtener los datos.
        return productoRepository.findAll();
    }
}
