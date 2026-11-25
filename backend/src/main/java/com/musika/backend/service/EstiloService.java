package com.musika.backend.service;

import java.util.List;

import com.musika.backend.model.Estilos;

public interface EstiloService {
    public List<Estilos> obtenerTodos();

    public Boolean existeEstilo(Integer id);

    public Estilos insertar(Estilos aut);

    public Boolean modificar(Estilos aut);

    public Boolean borrar(Integer id);
}
