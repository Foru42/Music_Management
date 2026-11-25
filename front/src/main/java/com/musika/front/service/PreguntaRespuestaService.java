package com.musika.front.service;

import com.musika.front.model.PreguntaRespuesta;

public interface PreguntaRespuestaService {

    public Boolean insertarPreguntaRespuesta(PreguntaRespuesta respuesta);

    public Boolean modificarPreguntaRespuesta(String token, PreguntaRespuesta respuesta);
}