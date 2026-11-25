package com.musika.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.model.Estilos;
import com.musika.backend.service.EstiloService;

@RestController
@RequestMapping("${api.estilos}")
public class EstiloController {

    @Autowired
    private EstiloService estiService;

    @GetMapping
    public ResponseEntity<List<Estilos>> listar() {
        List<Estilos> lista = estiService.obtenerTodos();

        return ResponseEntity.ok(lista);
    }
}
