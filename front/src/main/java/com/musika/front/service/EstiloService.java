package com.musika.front.service;

import java.util.List;

import com.musika.front.model.Estilo;

public interface EstiloService {
    public List<Estilo> obtenerTodos(String token);
}
