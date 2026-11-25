package com.musika.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.model.Preguntas;
import com.musika.backend.repository.PreguntasRepository;

@Service
public class PreguntaServiceImp implements PreguntaService {
    @Autowired
    private PreguntasRepository preRepo;

    @Override
    public List<Preguntas> obtenerTodos() {
        List<Preguntas> listaPreguntas = null;
        try {
            listaPreguntas = preRepo.findAll();
        } catch (Exception e) {

            return null;
        }
        return listaPreguntas;
    }

    @Override
    public Boolean existePregunta(Integer id) {
        try {
            return preRepo.existsById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Preguntas insertar(Preguntas aut) {
        Preguntas listaPreguntas = null;

        try {
            if (aut != null && aut.getId() == null) {
                listaPreguntas = preRepo.save(aut);
                return listaPreguntas;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean modificar(Preguntas aut) {
        try {
            if (preRepo.existsById(aut.getId())) {
                preRepo.save(aut);
                return true;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Boolean borrar(Integer id) {
        try {
            if (preRepo.existsById(id)) {
                preRepo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

}
