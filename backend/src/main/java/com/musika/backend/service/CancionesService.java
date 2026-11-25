package com.musika.backend.service;

import java.util.List;

import com.musika.backend.model.Canciones;

public interface CancionesService {
    public List<Canciones> obtenerTodosConRolPermitido();

    public List<Canciones> buscarCancionMultiFiltro(String usuario, String estilo, Double min, Double max,
            Boolean reportada);

    public Boolean existeCanciones(Integer id);

    public Canciones obtenerCancionById(Integer id);

    public Canciones insertar(Canciones aut);

    public Boolean modificar(Canciones aut);

    public Boolean borrar(Integer id);

}
