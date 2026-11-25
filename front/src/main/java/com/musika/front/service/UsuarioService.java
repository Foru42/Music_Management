package com.musika.front.service;

import java.util.List;

import com.musika.front.model.Usuario;

public interface UsuarioService {
    List<Usuario> obtenerTodos(String token);

    // public Usuario obtenerOrdenadorById(String token, Integer id);

    Usuario insertar(Usuario aut);

    Usuario obtenerUsuarioByName(String token, String nombre);

    Usuario obtenerUsuarioById(String token, Integer nombre);

    Boolean modificar(String token, Usuario aut);

    Boolean bannearById(String token, Integer id);

    Boolean desbannearById(String token, Integer id);

    // public Boolean borrar(String token, Integer id);
}
