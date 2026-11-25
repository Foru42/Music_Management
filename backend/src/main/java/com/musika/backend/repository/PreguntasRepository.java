package com.musika.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musika.backend.model.Preguntas;

public interface PreguntasRepository extends JpaRepository<Preguntas, Integer> {

}
