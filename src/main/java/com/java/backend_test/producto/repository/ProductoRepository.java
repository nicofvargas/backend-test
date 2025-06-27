package com.java.backend_test.producto.repository;

import com.java.backend_test.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Le dice a Spring que esta es una interfaz de repositorio.
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    //parece que no hace nada pero al extender con el JPA tenemos todos los metodos de
    //manipulacion de db
}
