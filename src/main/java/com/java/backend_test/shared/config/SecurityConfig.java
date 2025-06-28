package com.java.backend_test.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF para APIs REST sin estado
                .csrf(csrf -> csrf.disable())
                // 2. Configurar las reglas de autorización de las peticiones
                .authorizeHttpRequests(auth -> auth
                        //reglas de seguridad
                        // 1. Permitir peticiones GET a /api/v1/productos a cualquier usuario autenticado
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").authenticated()
                        // 2. Permitir peticiones POST, PUT, DELETE solo a usuarios con el rol 'ADMIN'
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()
                        // 3. Cualquier otra petición no configurada explícitamente, denegarla (opcional pero seguro)
                        .anyRequest().denyAll()
                )
                // 3. Habilitar la autenticación básica HTTP
                .httpBasic(withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}