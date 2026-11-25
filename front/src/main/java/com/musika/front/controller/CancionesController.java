package com.musika.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.musika.front.model.Cancion;
import com.musika.front.model.Usuario;
import com.musika.front.service.CancionService;
import com.musika.front.service.EstiloService;
import com.musika.front.service.Mp3Service;
import com.musika.front.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/canciones")
@Controller
public class CancionesController {

    @Autowired
    private CancionService canService;

    @Autowired
    private EstiloService estiService;

    @Autowired
    private UsuarioService useService;

    @Autowired
    private Mp3Service mp3Service;

    @GetMapping("")
    public String menuCanciones(Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";
        }

        String user = (String) session.getAttribute("f_user");
        Usuario u = useService.obtenerUsuarioByName(token, user);
        if (u != null && u.getRolFK() != null && Integer.valueOf(1).equals(u.getRolFK().getId())) {
            return "redirect:/admin";
        }

        model.addAttribute("estilos", estiService.obtenerTodos(token));
        model.addAttribute("canciones", canService.obtenerTodos(token));
        model.addAttribute("user_logged", useService.obtenerUsuarioByName(token, user));

        return "canciones";
    }

    @PostMapping("/nueva")
    public String guardarCancion(@ModelAttribute("cancion") Cancion cancion, Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");

        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            cancion.setReportada(false);

            Boolean resultado = canService.insertar(token, cancion, user);

            if (resultado) {
                return "redirect:/canciones";
            } else {
                model.addAttribute("error", "No se a insertado ninguna marca.");
                return "redirect:/canciones";
            }

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }

            model.addAttribute("error", "No se ha podido insertar la Cancion. Comprueba los datos.");

        } catch (RestClientException e) {
            model.addAttribute("error", "No se puede conectar con el servidor.");
        }

        return "redirect:/canciones";
    }

    @GetMapping("/borrar")
    public String borrarCancion(@RequestParam Integer id, RedirectAttributes redirect, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/";
        }

        try {
            Boolean resultado = canService.borrar(token, id);
            if (resultado) {
                redirect.addFlashAttribute("f_mensaje", "Cancion borrado correctamente");
            }

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }

            if (e.getStatusCode().value() == 404) {
                redirect.addFlashAttribute("f_mensaje", "La Cancion no existe o ya ha sido borrado");
            }
            if (e.getStatusCode().value() == 401) {
                redirect.addFlashAttribute("f_mensaje", "No tienes permisos de administrador");
            } else {
                redirect.addFlashAttribute("f_mensaje", "No se ha podido borrar la cancion");
            }

        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return "redirect:/canciones";
    }

    @GetMapping("/modificar")
    public String insertarModificarCancion(@RequestParam Integer id, Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/";
        }

        try {
            Cancion cancion = canService.obtenerCancionById(token, id);

            if (cancion == null) {
                model.addAttribute("f_mensaje", "La cancion no existe.");
                return "redirect:/canciones";
            }
            System.out.println("HEyy");
            model.addAttribute("estilos", estiService.obtenerTodos(token));
            model.addAttribute("cancion", cancion);

            return "modificarCanciones";

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }

            if (e.getStatusCode().value() == 404) {
                model.addAttribute("f_mensaje", "Error con el servidorf.");
                return "redirect:/canciones";
            }

            model.addAttribute("f_mensaje", "No se a podido insertar.");
            return "redirect:/canciones";

        } catch (RestClientException e) {
            return "redirect:/canciones";
        }
    }

    @PostMapping("/modificar")
    public String guardarCambiosCancion(@ModelAttribute("cancion") Cancion cancion, Model model,
            HttpSession session, RedirectAttributes redirect) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");

        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            Boolean resultado = canService.modificar(token, cancion, user);

            if (Boolean.TRUE.equals(resultado)) {
                redirect.addFlashAttribute("f_mensaje", "Cancion guardado correctamente");
                return "redirect:/canciones";
            }
            return "redirect:/canciones";

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            if (e.getStatusCode().value() == 401) {
                redirect.addFlashAttribute("f_mensaje", "No tienes permisos de administrador");
            }

            redirect.addFlashAttribute("f_mensaje", "Error al modificar la cancion");
            return "redirect:/canciones";

        } catch (RestClientException e) {
            model.addAttribute("f_mensaje", "No se puede conectar con el servidor.");
            return "redirect:/canciones";
        }
    }

    @PostMapping("/mp3")
    public Object convertirMp3(@RequestParam String url, HttpSession session, RedirectAttributes redirect) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            ResponseEntity<byte[]> respuesta = mp3Service.convertir(token, url);
            byte[] mp3 = respuesta.getBody();

            if (mp3 == null || mp3.length == 0) {
                redirect.addFlashAttribute("f_mensaje", "No se ha podido convertir el enlace");
                return "redirect:/canciones";
            }

            String disposition = respuesta.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            if (disposition == null) {
                disposition = ContentDisposition.attachment().filename("cancion.mp3").build().toString();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                    .contentLength(mp3.length)
                    .body(mp3);

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            System.out.println(e);
            redirect.addFlashAttribute("f_mensaje", "No se ha podido convertir el enlace");

        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return "redirect:/canciones";
    }
}
