package com.musika.front.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.musika.front.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestParam String user, @RequestParam String pass, Model model, HttpSession session) {

        try {
            Map<String, ?> respuesta = loginService.login(user, pass);

            if (respuesta == null || respuesta.get("token") == null) {
                model.addAttribute("error", "El servidor no ha devuelto el token.");
                return "home";
            }

            session.setAttribute("token", respuesta.get("token"));
            session.setAttribute("f_user", respuesta.get("user"));
            session.setAttribute("rol", respuesta.get("rol"));

       
            return "redirect:/canciones";

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 401) {
                model.addAttribute("error", "Usuario o contraseña incorrectos.");
            } else if (e.getStatusCode().value() == 403) {
                model.addAttribute("error", "Tu cuenta está bloqueada o baneada.");
            } else if (e.getStatusCode().value() == 400) {
                model.addAttribute("error", "Introduce usuario y contraseña.");
            } else {
                model.addAttribute("error", "Error al iniciar sesión.");
            }

            return "home";

        } catch (RestClientException e) {
            model.addAttribute("error", "No se puede conectar con el servidor de login.");
            return "home";
        }
    }
}
