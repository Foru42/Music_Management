package com.musika.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musika.backend.model.Roles;
import com.musika.backend.repository.RolesRepository;

@Service
public class RolesServiceImp implements RolesService {

    @Autowired
    private RolesRepository rolRepo;

    @Override
    public List<Roles> obtenerTodos() {
        List<Roles> listaRoles = null;
        try {
            listaRoles = rolRepo.findAll();
        } catch (Exception e) {

            return null;
        }

        return listaRoles;
    }

    @Override
    public Boolean existeRol(Integer id) {
        try {
            return rolRepo.existsById(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    @Override
    public Roles insertar(Roles aut) {
        Roles listaRoles = null;

        try {
            if (aut != null && aut.getId() == null) {
                listaRoles = rolRepo.save(aut);
                return listaRoles;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean modificar(Roles aut) {
        try {
            if (rolRepo.existsById(aut.getId())) {
                rolRepo.save(aut);
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
            if (rolRepo.existsById(id)) {
                rolRepo.deleteById(id);
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
