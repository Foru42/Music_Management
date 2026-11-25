package com.musika.front.auxiliar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class ImagenAuxiliar {
    public static Boolean BorrarImagen(String nombreImagen, String ruta) {

        try {
            if (nombreImagen == null || nombreImagen.isBlank() || nombreImagen.equals("default.jpg")) {
                return true;
            }

            Path carpeta = Paths.get(ruta).toAbsolutePath().normalize();
            Path imagen = carpeta.resolve(nombreImagen).normalize();

            if (!imagen.startsWith(carpeta)) {
                return false;
            }

            Files.deleteIfExists(imagen);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String GuardarImagen(MultipartFile archivoFoto, String ruta) throws IOException {
        String nombreFichero = UUID.randomUUID() + "_" + archivoFoto.getOriginalFilename();

        Path carpeta = Paths.get(ruta);

        Files.copy(archivoFoto.getInputStream(), carpeta.resolve(nombreFichero),
                StandardCopyOption.REPLACE_EXISTING);

        return nombreFichero;
    }
}
