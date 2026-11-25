package com.musika.backend.service;

import org.springframework.stereotype.Service;

import com.musika.backend.model.PreguntasRespuestas;

@Service
public interface PreguntasRespuestasService {

    Boolean insertarPreguntaRespuesta(PreguntasRespuestas aut);

    Boolean modificarPreguntaRespuesta(PreguntasRespuestas respuesta);

}
