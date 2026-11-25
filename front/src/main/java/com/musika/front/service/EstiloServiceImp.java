package com.musika.front.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.musika.front.model.Estilo;

@Service 
public class EstiloServiceImp implements EstiloService {
    @Value("${url.api.estilos}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Estilo> obtenerTodos(String token) {
        List<Estilo> listEstilos = restClient.get()
                .uri(URL)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Estilo>>() {
                });

        return listEstilos != null ? listEstilos : Collections.emptyList();
    }
}
