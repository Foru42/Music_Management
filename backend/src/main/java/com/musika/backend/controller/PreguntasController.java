package com.musika.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.model.Preguntas;
import com.musika.backend.model.Usuarios;
import com.musika.backend.repository.PreguntasRepository;
import com.musika.backend.service.PreguntaService;

@RestController
@RequestMapping("${api.preguntas}")
public class PreguntasController {

    @Autowired
    private PreguntaService preService;

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Preguntas> lista = preService.obtenerTodos();

        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.ok("Error Lista vacia");
        }

    }
}
