package com.musika.front.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.musika.front.model.Cancion;
import com.musika.front.model.Role;
import com.musika.front.model.Usuario;
import com.musika.front.service.CancionService;
import com.musika.front.service.EstiloService;
import com.musika.front.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private static final int ROL_ADMIN = 1;
    private static final int INTENTOS_BANEO = 4;

    @Autowired
    private CancionService canService;

    @Autowired
    private EstiloService estiService;

    @Autowired
    private UsuarioService usuService;

    private Usuario obtenerAdmin(String token, String user) {
        Usuario u = usuService.obtenerUsuarioByName(token, user);
        if (u == null || u.getRolFK() == null
                || !Integer.valueOf(ROL_ADMIN).equals(u.getRolFK().getId())) {
            return null;
        }
        return u;
    }

    @GetMapping("")
    public String menu(Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            Usuario admin = obtenerAdmin(token, user);
            if (admin == null) {
                return "redirect:/canciones";
            }
            model.addAttribute("user_logged", admin);
            return "adminMenu";

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
            }
            return "redirect:/";
        } catch (RestClientException e) {
            return "redirect:/";
        }
    }

    @PostMapping("/canciones/borrar")
    public String borrarCancion(@RequestParam Integer id, HttpSession session, RedirectAttributes redirect) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            if (obtenerAdmin(token, user) == null) {
                return "redirect:/canciones";
            }

            canService.borrar(token, id);
            redirect.addFlashAttribute("f_mensaje", "Canción borrada correctamente");

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            if (e.getStatusCode().value() == 404) {
                redirect.addFlashAttribute("f_mensaje", "La canción no existe o ya ha sido borrada");
            } else {
                redirect.addFlashAttribute("f_mensaje", "No se ha podido borrar la canción");
            }
        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return "redirect:/admin/canciones";
    }

    @PostMapping("/usuarios/banear")
    public String banearUsuario(@RequestParam Integer id,
            @RequestParam(defaultValue = "canciones") String origen,
            HttpSession session, RedirectAttributes redirect) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        String destino = "redirect:/admin/" + ("usuarios".equals(origen) ? "usuarios" : "canciones");

        try {
            Usuario admin = obtenerAdmin(token, user);
            if (admin == null) {
                return "redirect:/canciones";
            }

            Usuario objetivo = usuService.obtenerUsuarioById(token, id);
            if (objetivo == null) {
                redirect.addFlashAttribute("f_mensaje", "El usuario no existe");
                return destino;
            }

            if (objetivo.getId().equals(admin.getId())) {
                redirect.addFlashAttribute("f_mensaje", "No puedes banearte a ti mismo");
                return destino;
            }

            if (objetivo.getRolFK() != null && Integer.valueOf(ROL_ADMIN).equals(objetivo.getRolFK().getId())) {
                redirect.addFlashAttribute("f_mensaje", "No puedes banear a otro administrador");
                return destino;
            }

            usuService.bannearById(token, objetivo.getId());
            redirect.addFlashAttribute("f_mensaje", "Usuario baneado correctamente");

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            redirect.addFlashAttribute("f_mensaje", "No se ha podido banear al usuario");
        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return destino;
    }

    @GetMapping("/canciones")
    public String canciones(@RequestParam(required = false) String usuario,
            @RequestParam(required = false) String estilo,
            @RequestParam(required = false) String duracion,
            @RequestParam(required = false) String reportada,
            Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            Usuario admin = obtenerAdmin(token, user);
            if (admin == null) {
                return "redirect:/canciones";
            }

            Double min = null, max = null;
            if (duracion != null && duracion.contains("-")) {
                String[] r = duracion.split("-");
                min = Double.valueOf(r[0]);
                max = Double.valueOf(r[1]);
            }

            List<Cancion> canciones = canService.filtrarAdmin(token,
                    usuario == null || usuario.isBlank() ? null : usuario.trim(),
                    estilo == null || estilo.isBlank() ? null : estilo, min, max,
                    reportada == null || reportada.isBlank() ? null : Boolean.valueOf(reportada));

            model.addAttribute("user_logged", admin);
            model.addAttribute("estilos", estiService.obtenerTodos(token));
            model.addAttribute("canciones", canciones);
            model.addAttribute("limiteBaneo", INTENTOS_BANEO);

            model.addAttribute("fUsuario", usuario);
            model.addAttribute("fEstilo", estilo);
            model.addAttribute("fDuracion", duracion);
            model.addAttribute("fReportada", reportada);
            return "adminCanciones";

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
            }
            return "redirect:/";
        } catch (RestClientException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            Usuario admin = obtenerAdmin(token, user);
            if (admin == null) {
                return "redirect:/canciones";
            }

            model.addAttribute("user_logged", admin);
            model.addAttribute("usuarios", usuService.obtenerTodos(token));
            model.addAttribute("limiteBaneo", INTENTOS_BANEO);
            return "adminUsuarios";

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
            }
            return "redirect:/";
        } catch (RestClientException e) {
            return "redirect:/";
        }
    }

    @PostMapping("/usuarios/desbanear")
    public String desbanearUsuario(@RequestParam Integer id, HttpSession session, RedirectAttributes redirect) {

        String token = (String) session.getAttribute("token");
        String user = (String) session.getAttribute("f_user");
        if (token == null || user == null) {
            return "redirect:/";
        }

        try {
            if (obtenerAdmin(token, user) == null) {
                return "redirect:/canciones";
            }

            Usuario desbaneo = new Usuario();
            desbaneo.setId(id);
            desbaneo.setIntentos(0);

            Role rol = new Role();
            rol.setId(2);
            desbaneo.setRolFK(rol);
            usuService.modificar(token, desbaneo);

            redirect.addFlashAttribute("f_mensaje", "Usuario desbaneado correctamente");

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                session.invalidate();
                return "redirect:/";
            }
            redirect.addFlashAttribute("f_mensaje", "No se ha podido desbanear al usuario");
        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return "redirect:/admin/usuarios";
    }
}