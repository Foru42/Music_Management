package com.musika.front.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.musika.front.model.Usuario;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Value("${url.api.usuario}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Usuario> obtenerTodos(String token) {

        List<Usuario> listUsuarios = restClient.get()
                .uri(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Usuario>>() {
                });

        return listUsuarios != null ? listUsuarios : Collections.emptyList();
    }

    @Override
    public Usuario insertar(Usuario user) {

        return restClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(Usuario.class);
    }

    @Override
    public Usuario obtenerUsuarioByName(String token, String name) {

        return restClient.get()
                .uri(URL + "/buscar" + "?user={user}", name)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Usuario.class);
    }

    @Override
    public Usuario obtenerUsuarioById(String token, Integer id) {

        return restClient.get()
                .uri(URL + "/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Usuario.class);
    }

    @Override
    public Boolean modificar(String token, Usuario usuario) {

        restClient.put()
                .uri(URL + "/modificar")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuario)
                .retrieve()
                .toBodilessEntity();

        return true;
    }

    @Override
    public Boolean bannearById(String token, Integer id) {

        restClient.put()
                .uri(URL + "/bannear/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        return true;
    }

    @Override
    public Boolean desbannearById(String token, Integer id) {

        restClient.put()
                .uri(URL + "/desbanear/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        return true;
    }
}
