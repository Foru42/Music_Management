package com.musika.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canciones")
public class Canciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", length = 255, nullable = false)
    private String titulo;

    @Column(name = "enlace", length = 255, nullable = false)
    private String enlace;

    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @ManyToOne
    @JoinColumn(name = "estilo_fk", nullable = false)
    private Estilos estiloFK;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "reportada", nullable = false)
    private Boolean reportada;

    @ManyToOne
    @JoinColumn(name = "usuario_fk", nullable = false)
    @JsonIgnoreProperties({ "pass", "salt", "intentos", "preguntas", "rolFK" })
    private Usuarios usuarioFk;
}
