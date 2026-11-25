package com.musika.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.model.PreguntasRespuestas;
import com.musika.backend.service.PreguntasRespuestasService;

@RestController
@RequestMapping("${api.preguntasRespuestas}")
public class PreguntasRespuestasController {

    @Autowired
    private PreguntasRespuestasService preResService;

    @PostMapping
    public ResponseEntity<Boolean> insertar(@RequestBody PreguntasRespuestas respuesta) {

        Boolean resultado = preResService.insertarPreguntaRespuesta(respuesta);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true);
        }

        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Boolean> modificarPreguntaRespuesta(@RequestBody PreguntasRespuestas respuesta) {

        Boolean resultado = preResService.modificarPreguntaRespuesta(respuesta);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.badRequest().body(false);
    }
}
