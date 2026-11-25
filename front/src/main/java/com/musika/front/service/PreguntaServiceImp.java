package com.musika.front.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.musika.front.model.Pregunta;

@Service
public class PreguntaServiceImp implements PreguntaService {
    @Value("${url.api.preguntas}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Pregunta> obtenerTodos() {

        List<Pregunta> listPreguntas = restClient.get()
                .uri(URL)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Pregunta>>() {
                });

        return listPreguntas != null ? listPreguntas : Collections.emptyList();
    }

}
