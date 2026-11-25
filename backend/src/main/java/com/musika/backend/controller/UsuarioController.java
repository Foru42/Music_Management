package com.musika.backend.controller;

import java.util.List;
import java.util.Map;

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

import com.musika.backend.model.Usuarios;
import com.musika.backend.service.UsuarioService;

@RestController
@RequestMapping("${api.usuarios}")
public class UsuarioController {
    @Autowired
    private UsuarioService useService;

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<Usuarios> lista = useService.obtenerTodos();

        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.ok("Error Lista vacia");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerUsuarioById(@PathVariable Integer id) {

        Usuarios usu = useService.obtenerUsuarioById(id);

        if (usu == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usu);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Usuarios> obtenerUsuarioByName(@RequestParam("user") String user) {

        Usuarios usu = useService.buscarPorUser(user);

        if (usu == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usu);
    }

    @PostMapping
    public ResponseEntity<?> insertarUsuarios(@RequestBody Usuarios usu) {
        if (usu.getUser() == null || usu.getUser().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario y contraseña son obligatorios."));
        }

        if (useService.buscarPorUser(usu.getUser()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El usuario ya existe."));
        }

        Usuarios guardado = useService.insertar(usu);

        if (guardado != null && guardado.getId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarUsuario(@RequestBody Usuarios user) {

        if (user == null || user.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El ID del usuario es obligatorio."));
        }

        Usuarios usuario = useService.obtenerUsuarioById(user.getId());

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getIntentos() != null && user.getIntentos() >= 0) {
            usuario.setIntentos(user.getIntentos());
        }

        if (user.getRolFK() != null && user.getRolFK().getId() != null && user.getRolFK().getId() >= 1
                && user.getRolFK().getId() <= 4) {

            usuario.setRolFK(user.getRolFK());
        }

        if (user.getUser() != null && !user.getUser().isBlank()) {

            Usuarios existente = useService.buscarPorUser(user.getUser());

            if (existente != null && !existente.getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El usuario ya existe."));
            }

            usuario.setUser(user.getUser().trim());
        }

        if (user.getFoto() != null && !user.getFoto().isBlank()) {
            usuario.setFoto(user.getFoto().trim());
        }

        Boolean resultado = useService.modificar(usuario);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.ok(usuario);
        }

        return ResponseEntity.badRequest()
                .body(Map.of("error", "No se ha podido modificar el usuario."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Integer id) {

        if (!useService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        Boolean resultado = useService.borrar(id);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/bannear/{id}")
    public ResponseEntity<Void> bannearUsuario(@PathVariable Integer id) {

        if (!useService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        Boolean resultado = useService.bannearUsuario(id);

        if (Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/desbanear/{id}")
    public ResponseEntity<Void> desbanearUsuario(@PathVariable Integer id) {

        if (!useService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        Boolean resultado = useService.gestionarRolUsuarios(id, 2);

        if (!Boolean.TRUE.equals(resultado)) {
            return ResponseEntity.badRequest().build();
        }

        Boolean intentosReiniciados = useService.reiniciarIntentos(id);

        if (!Boolean.TRUE.equals(intentosReiniciados)) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.noContent().build();
    }

}
