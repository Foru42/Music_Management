package com.musika.front.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LoginServiceImp implements LoginService {

    private final RestClient restClient = RestClient.create();

    @Value("${url.api.login}")
    private String URL;

    @Override
    public Map<String, ?> login(String user, String pass) {
        return restClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("user", user, "pass", pass))
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

}
