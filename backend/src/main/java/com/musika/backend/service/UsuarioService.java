package com.musika.backend.service;

import java.util.List;

import com.musika.backend.model.Usuarios;

public interface UsuarioService {
    public Usuarios insertar(Usuarios user);

    public Usuarios buscarPorUser(String user);

    public boolean validarPassword(String password, Usuarios usuario);

    public List<Usuarios> obtenerTodos();

    public Boolean existeUsuario(Integer id);

    public Usuarios obtenerUsuarioById(Integer id);

    public Boolean modificar(Usuarios aut);

    public Boolean borrar(Integer id);

    public Boolean bloquearUsuario(Integer id);

    public Boolean bannearUsuario(Integer id);

    public Boolean gestionarRolUsuarios(Integer idusu, Integer idRol);

    public Boolean incrementarIntentos(Integer id);

    public Boolean reiniciarIntentos(Integer id);

}
