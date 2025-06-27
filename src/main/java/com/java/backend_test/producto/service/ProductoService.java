package com.java.backend_test.producto.service;

import com.java.backend_test.producto.Producto;
import com.java.backend_test.producto.dto.ProductoRequest;
import com.java.backend_test.producto.dto.ProductoResponse;
import com.java.backend_test.producto.repository.ProductoRepository;
import com.java.backend_test.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service // Le dice a Spring que esta clase es un componente de servicio.
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección de dependencias por constructor (la mejor práctica).
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Page<ProductoResponse> obtenerTodosLosProductos(Pageable pageable) {
        // 1. Llamamos al método findAll del repositorio, que ya soporta Pageable.
        Page<Producto> productosPage = productoRepository.findAll(pageable);

        // 2. El objeto Page tiene un método .map() para convertir su contenido.
        //    Mapeamos cada entidad Producto a su DTO ProductoResponse.
        return productosPage.map(producto -> new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio()
        ));
    }

    public ProductoResponse guardarProducto(ProductoRequest productoRequest) {
        Producto producto = new Producto();
        producto.setNombre(productoRequest.nombre());
        producto.setPrecio(productoRequest.precio());

        // Guardamos la entidad
        Producto productoGuardado = productoRepository.save(producto);

        // Mapeamos la entidad guardada a un DTO de respuesta y lo devolvemos
        return new ProductoResponse(productoGuardado.getId(), productoGuardado.getNombre(), productoGuardado.getPrecio());
    }

    public ProductoResponse actualizarProducto(Long id, ProductoRequest productoRequest) {
        // 1. Buscar el producto existente por su ID.
        //    Usamos orElseThrow para lanzar una excepción si no se encuentra.
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + id));

        // 2. Actualizar los campos de la entidad con los datos del DTO.
        productoExistente.setNombre(productoRequest.nombre());
        productoExistente.setPrecio(productoRequest.precio());

        // 3. Guardar la entidad actualizada. Spring Data JPA es inteligente:
        //    como la entidad ya tiene un ID, ejecutará un UPDATE en lugar de un INSERT.
        Producto productoActualizado = productoRepository.save(productoExistente);

        // 4. Mapear la entidad actualizada a un DTO de respuesta y devolverlo.
        return new ProductoResponse(
                productoActualizado.getId(),
                productoActualizado.getNombre(),
                productoActualizado.getPrecio()
        );
    }

    public void eliminarProducto(Long id) {
        // 1. Verificamos si el producto existe antes de intentar borrarlo.
        //    Si no existe, findById lanzará la excepción que ya configuramos.
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con el id: " + id);
        }

        // 2. Si existe, procedemos a eliminarlo.
        productoRepository.deleteById(id);
    }
}
