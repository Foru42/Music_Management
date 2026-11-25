package com.musika.backend.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.service.Mp3Service;

@RestController
@RequestMapping("${api.cancion.mp3}")
public class Mp3Controller {

    public record Mp3Request(String url) {
    }

    @Autowired
    private Mp3Service mp3Service;

    @PostMapping
    public ResponseEntity<byte[]> descargarMp3(@RequestBody Mp3Request request) throws Exception {

        Path archivo = mp3Service.descargar(request.url());

        try {
            byte[] contenido = Files.readAllBytes(archivo);
            String nombre = archivo.getFileName().toString();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename(nombre, StandardCharsets.UTF_8)
                                    .build().toString())
                    .contentLength(contenido.length)
                    .body(contenido);
        } finally {
            mp3Service.eliminarTemporal(archivo);
        }
    }
}
