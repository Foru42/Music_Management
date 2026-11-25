package com.musika.front.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.musika.front.auxiliar.ImagenAuxiliar;
import com.musika.front.model.Pregunta;
import com.musika.front.model.PreguntaRespuesta;
import com.musika.front.model.Usuario;
import com.musika.front.service.PreguntaRespuestaService;
import com.musika.front.service.PreguntaService;
import com.musika.front.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/usuario")
@Controller
public class UsuarioController {

    @Value("${ruta.imagenes}")
    private String ruta;

    @Autowired
    private UsuarioService usuService;

    @Autowired
    private PreguntaService preService;

    @Autowired
    private PreguntaRespuestaService preResService;

    @GetMapping("/nuevo")
    public String inicio(Model model) {

        List<Pregunta> listaPreguntas = preService.obtenerTodos();
        model.addAttribute("preguntas", listaPreguntas);

        return "nuevoUsuario";
    }

    @PostMapping("/insertar")
    public String insertarUsuarios(@ModelAttribute("usuario") Usuario usuario,
            @RequestParam(value = "archivoFoto", required = false) MultipartFile archivoFoto,
            Model model, HttpSession session) throws IOException {

        List<PreguntaRespuesta> preguntas = usuario.getPreguntas();
        usuario.setPreguntas(null);

        try {
            if (archivoFoto != null && !archivoFoto.isEmpty()) {
                usuario.setFoto(ImagenAuxiliar.GuardarImagen(archivoFoto, ruta));
            }

            Usuario usuarioInsertado = usuService.insertar(usuario);

            if (usuarioInsertado == null || usuarioInsertado.getId() == null) {
                ImagenAuxiliar.BorrarImagen(usuario.getFoto(), ruta);
                model.addAttribute("error", "No se ha insertado ningún usuario.");
                model.addAttribute("preguntas", preService.obtenerTodos());
                return "nuevoUsuario";
            }

            if (preguntas != null) {
                for (PreguntaRespuesta respuesta : preguntas) {

                    if (respuesta != null && respuesta.getPreguntaId() != null && respuesta.getRespuesta() != null
                            && !respuesta.getRespuesta().isBlank()) {

                        respuesta.setUsuarioId(usuarioInsertado.getId());
                        preResService.insertarPreguntaRespuesta(respuesta);
                    }
                }
            }

            return "redirect:/";

        } catch (RestClientResponseException e) {

            if (e.getStatusCode().value() == 401
                    || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }

            ImagenAuxiliar.BorrarImagen(usuario.getFoto(), ruta);

            model.addAttribute("error", "No se ha podido insertar el usuario.");
            model.addAttribute("preguntas", preService.obtenerTodos());
            return "nuevoUsuario";

        } catch (RestClientException e) {
            model.addAttribute("error", "No se puede conectar con el servidor.");
            model.addAttribute("preguntas", preService.obtenerTodos());
            return "nuevoUsuario";
        }
    }

    @GetMapping("/perfil")
    public String insertarModificarUsuario(Model model, HttpSession session) {

        String user = (String) session.getAttribute("f_user");
        String token = (String) session.getAttribute("token");

        if (token == null || user == null) {
            return "redirect:/";
        }

        try {

            model.addAttribute("preguntas", preService.obtenerTodos());
            model.addAttribute("usuario", usuService.obtenerUsuarioByName(token, user));

            return "modificarUsuario";

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
    public String modificarUsuario(@ModelAttribute("usuario") Usuario form,
            @RequestParam(value = "archivoFoto", required = false) MultipartFile archivoFoto,
            HttpSession session, RedirectAttributes redirect) throws IOException {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {

            Usuario actual = usuService.obtenerUsuarioByName(token, user);
            String fotoAntigua = actual.getFoto();

            if (archivoFoto != null && !archivoFoto.isEmpty()) {
                actual.setFoto(ImagenAuxiliar.GuardarImagen(archivoFoto, ruta));
            }

            List<PreguntaRespuesta> respuestas = form.getPreguntas();
            actual.setPreguntas(null);

            usuService.modificar(token, actual);

            if (respuestas != null) {
                for (PreguntaRespuesta r : respuestas) {
                    if (r != null && r.getPreguntaId() != null
                            && r.getRespuesta() != null && !r.getRespuesta().isBlank()) {
                        r.setUsuarioId(actual.getId());
                        if (r.getId() != null) {
                            preResService.modificarPreguntaRespuesta(token, r);
                        } else {
                            preResService.insertarPreguntaRespuesta(r);
                        }
                    }
                }
            }

            if (fotoAntigua != null && !fotoAntigua.equals(actual.getFoto())) {
                ImagenAuxiliar.BorrarImagen(fotoAntigua, ruta);
            }

            session.setAttribute("f_foto", actual.getFoto());
            redirect.addFlashAttribute("f_mensaje", "Perfil actualizado correctamente");
            return "redirect:/canciones";

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            redirect.addFlashAttribute("f_mensaje", "No se ha podido actualizar el perfil");
        } catch (RestClientException e) {

            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servido1r");
        }

        return "redirect:/usuario/perfil";
    }
}