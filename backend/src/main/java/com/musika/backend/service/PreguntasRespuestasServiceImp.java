package com.musika.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.auxiliar.LoginAuxiliar;
import com.musika.backend.model.Preguntas;
import com.musika.backend.model.PreguntasRespuestas;
import com.musika.backend.model.Usuarios;
import com.musika.backend.repository.PreguntasRepository;
import com.musika.backend.repository.PreguntasRespuestasRepository;
import com.musika.backend.repository.UsuariosRepository;

@Service
public class PreguntasRespuestasServiceImp implements PreguntasRespuestasService {

    @Autowired
    private PreguntasRespuestasRepository preresRepo;

    @Autowired
    private UsuariosRepository usuariosRepo;

    @Autowired
    private PreguntasRepository preguntasRepo;

    @Override
    public Boolean insertarPreguntaRespuesta(PreguntasRespuestas respuesta) {
        try {
            if (respuesta == null || respuesta.getUsuario() == null || respuesta.getPregunta() == null
                    || respuesta.getRespuesta() == null || respuesta.getRespuesta().isBlank()) {
                return false;
            }

            Usuarios usuario = usuariosRepo.findById(respuesta.getUsuario().getId()).orElse(null);

            Preguntas pregunta = preguntasRepo.findById(respuesta.getPregunta().getId()).orElse(null);

            if (usuario == null || pregunta == null) {
                return false;
            }

            PreguntasRespuestas nuevaRespuesta = new PreguntasRespuestas();
            nuevaRespuesta.setUsuario(usuario);
            nuevaRespuesta.setPregunta(pregunta);
            nuevaRespuesta.setRespuesta(LoginAuxiliar.hashear(respuesta.getRespuesta() + usuario.getSalt()));

            preresRepo.save(nuevaRespuesta);

            return true;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean modificarPreguntaRespuesta(PreguntasRespuestas respuesta) {
        try {
            if (respuesta == null || respuesta.getId() == null
                    || respuesta.getUsuario() == null || respuesta.getUsuario().getId() == null
                    || respuesta.getPregunta() == null || respuesta.getPregunta().getId() == null
                    || respuesta.getRespuesta() == null || respuesta.getRespuesta().isBlank()) {
                return false;
            }

            PreguntasRespuestas existente = preresRepo.findById(respuesta.getId()).orElse(null);
            if (existente == null) {
                return false;
            }

            if (!existente.getUsuario().getId().equals(respuesta.getUsuario().getId())) {
                return false;
            }

            Preguntas pregunta = preguntasRepo.findById(respuesta.getPregunta().getId()).orElse(null);
            if (pregunta == null) {
                return false;
            }

            existente.setPregunta(pregunta);
            existente.setRespuesta(LoginAuxiliar.hashear(respuesta.getRespuesta() + existente.getUsuario().getSalt()));

            preresRepo.save(existente);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
