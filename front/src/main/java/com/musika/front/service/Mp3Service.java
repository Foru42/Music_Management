package com.musika.front.service;

import org.springframework.http.ResponseEntity;

public interface Mp3Service {
    ResponseEntity<byte[]> convertir(String token, String url);
}
