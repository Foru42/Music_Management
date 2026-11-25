package com.musika.front.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.musika.front.model.PreguntaRespuesta;

@Service
public class PreguntaRespuestaServiceImp implements PreguntaRespuestaService {

    @Value("${url.api.preguntasRespuesta}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public Boolean insertarPreguntaRespuesta(PreguntaRespuesta respuesta) {

        Map<String, Object> body = Map.of(
                "usuario", Map.of("id", respuesta.getUsuarioId()),
                "pregunta", Map.of("id", respuesta.getPreguntaId()),
                "respuesta", respuesta.getRespuesta());

        restClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        return true;
    }

    @Override
    public Boolean modificarPreguntaRespuesta(String token, PreguntaRespuesta preRes) {

        Map<String, Object> body = Map.of(
                "id", preRes.getId(),
                "usuario", Map.of("id", preRes.getUsuarioId()),
                "pregunta", Map.of("id", preRes.getPreguntaId()),
                "respuesta", preRes.getRespuesta());

        Boolean resultado = restClient.put()
                .uri(URL + "/modificar")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Boolean.class);

        return Boolean.TRUE.equals(resultado);
    }

}