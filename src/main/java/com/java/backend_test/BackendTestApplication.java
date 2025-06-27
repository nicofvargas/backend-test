package com.java.backend_test;

import com.java.backend_test.entity.Producto;
import com.java.backend_test.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BackendTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTestApplication.class, args);
	}


	// esto es solo para tener datos cargados temporalmente y probemos
	@Bean
	CommandLineRunner runner(ProductoRepository productoRepository) {
		return args -> {
			// Creamos algunos productos de ejemplo
			Producto producto1 = new Producto();
			producto1.setNombre("Laptop Pro");
			producto1.setPrecio(1200.50);

			Producto producto2 = new Producto();
			producto2.setNombre("Mouse Inalámbrico");
			producto2.setPrecio(25.00);

			Producto producto3 = new Producto();
			producto3.setNombre("Teclado Mecánico");
			producto3.setPrecio(99.99);

			// Los guardamos en la base de datos
			productoRepository.saveAll(List.of(producto1, producto2, producto3));
		};
	}

}
