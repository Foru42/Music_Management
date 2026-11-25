package com.musika.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.musika.backend.model.Canciones;

public interface CancionesRepository extends JpaRepository<Canciones, Integer> {

    @Query("""
                SELECT c FROM Canciones c
                WHERE (:usuario IS NULL OR LOWER(c.usuarioFk.user) LIKE LOWER(CONCAT('%', :usuario, '%')))
                AND (:estilo IS NULL OR c.estiloFK.nombre = :estilo)
                AND (:duracionMin IS NULL OR c.duracion >= :duracionMin)
                AND (:duracionMax IS NULL OR c.duracion <= :duracionMax)
                AND (:reportada IS NULL OR c.reportada = :reportada)
            """)
    List<Canciones> filtrarAdmin(@Param("usuario") String usuario, @Param("estilo") String estilo,
            @Param("duracionMin") Double duracionMin, @Param("duracionMax") Double duracionMax,
            @Param("reportada") Boolean reportada);

    @Query("SELECT c FROM Canciones c WHERE c.usuarioFk.rolFK.id IN (1, 2)")
    List<Canciones> findByRolesPermitidos();
}
