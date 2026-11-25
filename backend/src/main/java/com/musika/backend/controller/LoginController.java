package com.musika.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musika.backend.model.Usuarios;
import com.musika.backend.service.JwtService;
import com.musika.backend.service.UsuarioService;

@RestController
@RequestMapping("${api.login}")
public class LoginController {
    @Autowired
    private UsuarioService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String user = request.get("user");
        String pass = request.get("pass");

        if (user == null || pass == null || user.isBlank() || pass.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Introduce usuario y contraseña."));
        }

        Usuarios usuario = userService.buscarPorUser(user);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos."));
        }

        String rol = usuario.getRolFK().getNombre();

        if ("BLOQUEADO".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario bloqueado."));
        }

        if ("BANEADO".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario baneado."));
        }

        if (!userService.validarPassword(pass, usuario)) {

            userService.incrementarIntentos(usuario.getId());

            usuario = userService.buscarPorUser(user);

            if (usuario.getIntentos() >= 3) {
                userService.bloquearUsuario(usuario.getId());

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Usuario bloqueado."));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos."));
        }

        userService.reiniciarIntentos(usuario.getId());

        String token = jwtService.generarToken(usuario.getUser(), rol);

        return ResponseEntity
                .ok(Map.of("token", token, "user", usuario.getUser(), "rol", usuario.getRolFK().getNombre()));
    }
}
