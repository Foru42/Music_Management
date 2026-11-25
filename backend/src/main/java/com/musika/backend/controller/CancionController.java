package com.musika.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.model.Canciones;
import com.musika.backend.model.Usuarios;
import com.musika.backend.service.CancionesService;
import com.musika.backend.service.UsuarioService;

@RestController
@RequestMapping("${api.canciones}")
public class CancionController {

    @Autowired
    private CancionesService cancService;

    @Autowired
    private UsuarioService useService;

    @GetMapping
    public ResponseEntity<List<Canciones>> listar() {
        List<Canciones> lista = cancService.obtenerTodosConRolPermitido();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Canciones> obtenerCancionById(@PathVariable Integer id) {

        Canciones cancion = cancService.obtenerCancionById(id);

        if (cancion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancion);
    }

    @PostMapping
    public ResponseEntity<?> insertarCanciones(@RequestBody Canciones cancion, @RequestParam("user") String user) {

        Usuarios usuarioEncontrado = useService.buscarPorUser(user);

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado el usuario: " + user);
        }

        cancion.setUsuarioFk(usuarioEncontrado);

        Canciones guardado = cancService.insertar(cancion);

        if (guardado != null && guardado.getId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        }

        return ResponseEntity.badRequest().body("No se ha podido insertar la canción");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCancion(@PathVariable Integer id) {

        if (!cancService.existeCanciones(id)) {
            return ResponseEntity.notFound().build();
        }

        Boolean resultado = cancService.borrar(id);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/modificar")
    public ResponseEntity<Boolean> modificarCancion(@RequestBody Canciones cancion, @RequestParam("user") String user) {

        Usuarios usuarioEncontrado = useService.buscarPorUser(user);

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        cancion.setUsuarioFk(usuarioEncontrado);

        Boolean resultado = cancService.modificar(cancion);

        if (resultado) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/admin/filtrar")
    public ResponseEntity<List<Canciones>> filtrarAdmin(@RequestParam(required = false) String usuario,
            @RequestParam(required = false) String estilo, @RequestParam(required = false) Double duracionMin,
            @RequestParam(required = false) Double duracionMax, @RequestParam(required = false) Boolean reportada) {

        return ResponseEntity
                .ok(cancService.buscarCancionMultiFiltro(usuario, estilo, duracionMin, duracionMax, reportada));
    }
}
