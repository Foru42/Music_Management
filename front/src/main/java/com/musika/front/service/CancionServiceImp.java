package com.musika.front.service;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.musika.front.model.Cancion;

@Service
public class CancionServiceImp implements CancionService {
    @Value("${url.api.canciones}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Cancion> obtenerTodos(String token) {

        List<Cancion> listCanciones = restClient.get()
                .uri(URL)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Cancion>>() {
                });

        return listCanciones != null ? listCanciones : Collections.emptyList();
    }

    @Override
    public Boolean insertar(String token, Cancion can, String user) {

        restClient.post()
                .uri(URL + "?user={user}", user)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(can)
                .retrieve()
                .toBodilessEntity();

        return true;
    }

    @Override
    public Boolean borrar(String token, Integer id) {

        restClient.delete()
                .uri(URL + "/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        return true;
    }

    @Override
    public Cancion obtenerCancionById(String token, Integer id) {

        return restClient.get()
                .uri(URL + "/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Cancion.class);
    }

    @Override
    public Boolean modificar(String token, Cancion canciones, String user) {

        Boolean resultado = restClient.put()
                .uri(URL + "/modificar" + "?user={user}", user)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(canciones)
                .retrieve()
                .body(Boolean.class);

        return Boolean.TRUE.equals(resultado);
    }

    @Override
    public List<Cancion> filtrarAdmin(String token, String usuario, String estilo,
            Double min, Double max, Boolean reportada) {

        URI uri = UriComponentsBuilder.fromUriString(URL + "/admin/filtrar")
                .queryParamIfPresent("usuario", Optional.ofNullable(usuario))
                .queryParamIfPresent("estilo", Optional.ofNullable(estilo))
                .queryParamIfPresent("duracionMin", Optional.ofNullable(min))
                .queryParamIfPresent("duracionMax", Optional.ofNullable(max))
                .queryParamIfPresent("reportada", Optional.ofNullable(reportada))
                .encode().build().toUri();

        Cancion[] resultado = restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Cancion[].class);

        return resultado == null ? List.of() : List.of(resultado);
    }
}
