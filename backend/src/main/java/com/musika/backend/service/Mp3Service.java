package com.musika.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Mp3Service {

    private static final Pattern YOUTUBE_URL = Pattern.compile(
            "^https://((www|m|music)\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[\\w-]{11}([&?].*)?$");

    private static final int TIMEOUT_MINUTOS = 3;

    @Value("${ytdlp.path:yt-dlp}")
    private String ytdlpPath;

    @Value("${ffmpeg.location:}")
    private String ffmpegLocation;

    public Path descargar(String url) throws IOException, InterruptedException {
        if (url == null || !YOUTUBE_URL.matcher(url).matches()) {
            throw new IllegalArgumentException("URL de YouTube no válida");
        }

        Path dir = Files.createTempDirectory("mp3-");

        List<String> cmd = new ArrayList<>();
        cmd.add(ytdlpPath);
        cmd.add("-x"); // extraer solo audio
        cmd.add("--audio-format");
        cmd.add("mp3");
        cmd.add("--audio-quality");
        cmd.add("0"); // mejor calidad VBR
        cmd.add("--no-playlist"); // solo el vídeo, no la lista

        cmd.add("--embed-thumbnail");
        cmd.add("--embed-metadata");
        cmd.add("--convert-thumbnails");
        cmd.add("jpg"); // YouTube da webp, que muchos reproductores no leen
        cmd.add("--ppa");
        cmd.add("ThumbnailsConvertor:-vf crop=ih:ih"); // recorta la imagen 16:9 a cuadrada

        cmd.add("-o");
        cmd.add(dir.resolve("%(title).150s.%(ext)s").toString());
        if (!ffmpegLocation.isBlank()) {
            cmd.add("--ffmpeg-location");
            cmd.add(ffmpegLocation);
        }
        cmd.add("--"); // lo siguiente nunca se interpreta como opción
        cmd.add(url);

        Process proceso = new ProcessBuilder(cmd)
                .redirectErrorStream(true)
                .start();

        // Hay que leer la salida o el proceso puede bloquearse
        String salida = new String(proceso.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        if (!proceso.waitFor(TIMEOUT_MINUTOS, TimeUnit.MINUTES)) {
            proceso.destroyForcibly();
            borrarDirectorio(dir);
            throw new IOException("yt-dlp tardó demasiado");
        }
        if (proceso.exitValue() != 0) {
            borrarDirectorio(dir);
            throw new IOException("yt-dlp falló: " + salida);
        }

        try (Stream<Path> archivos = Files.list(dir)) {
            return archivos
                    .filter(p -> p.toString().endsWith(".mp3"))
                    .findFirst()
                    .orElseThrow(() -> new IOException("No se generó el mp3"));
        }
    }

    public void eliminarTemporal(Path archivo) {
        try {
            borrarDirectorio(archivo.getParent());
        } catch (IOException e) {
            // Aquí podrías loguearlo
        }
    }

    private void borrarDirectorio(Path dir) throws IOException {
        if (dir == null || !Files.exists(dir))
            return;
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
    }
}