package com.musika.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.model.Estilos;
import com.musika.backend.repository.EstilosRepository;

@Service
public class EstiloServiceImp implements EstiloService{
    @Autowired
    private EstilosRepository estiloRepo;

    @Override
    public List<Estilos> obtenerTodos() {
        List<Estilos> listaEstilos = null;
        try {
            listaEstilos = estiloRepo.findAll();
        } catch (Exception e) {

            return null;
        }

        return listaEstilos;
    }

    @Override
    public Boolean existeEstilo(Integer id) {
        try {
            return estiloRepo.existsById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Estilos insertar(Estilos aut) {
        Estilos listaEstilos = null;

        try {
            if (aut != null && aut.getId() == null) {
                listaEstilos = estiloRepo.save(aut);
                return listaEstilos;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean modificar(Estilos aut) {
        try {
            if (estiloRepo.existsById(aut.getId())) {
                estiloRepo.save(aut);
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
            if (estiloRepo.existsById(id)) {
                estiloRepo.deleteById(id);
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
