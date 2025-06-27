package com.java.backend_test.controller;

import com.java.backend_test.dto.MensajeResponse; //cargo el dto
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HolaMundoController {

    @GetMapping("/hola")
    public MensajeResponse saludar() {
        return new MensajeResponse("prueba de retorno","nico");
    }
}
