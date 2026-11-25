package com.musika.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musika.backend.model.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
    public Usuarios findByUser(String user);

}
