package com.musika.backend.service;

import java.util.List;

import com.musika.backend.model.Preguntas;

public interface PreguntaService {
    public List<Preguntas> obtenerTodos();

    public Boolean existePregunta(Integer id);

    public Preguntas insertar(Preguntas aut);

    public Boolean modificar(Preguntas aut);

    public Boolean borrar(Integer id);
}
