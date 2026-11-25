package com.musika.front.service;

import java.util.List;

import com.musika.front.model.Cancion;

public interface CancionService {
    List<Cancion> obtenerTodos(String token);

    Boolean insertar(String token, Cancion can, String user);

    Boolean borrar(String token, Integer id);

    Boolean modificar(String token, Cancion aut, String user);

    Cancion obtenerCancionById(String token, Integer id);

    List<Cancion> filtrarAdmin(String token, String usuario, String estilo, Double min, Double max, Boolean reportada);
}
