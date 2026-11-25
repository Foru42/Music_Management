package com.musika.backend.service;

import java.util.List;

import com.musika.backend.model.Roles;

public interface RolesService {
    public List<Roles> obtenerTodos();

    public Boolean existeRol(Integer id);

    public Roles insertar(Roles aut);

    public Boolean modificar(Roles aut);

    public Boolean borrar(Integer id);

}
