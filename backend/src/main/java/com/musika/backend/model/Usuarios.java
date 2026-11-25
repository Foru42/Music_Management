package com.musika.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user", length = 45, nullable = false)
    private String user;

    @Column(name = "pass", nullable = false)
    private String pass;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "intentos", nullable = false)
    private Integer intentos = 0;

    @Column(name = "foto", nullable = false, length = 255)
    private String foto;

    @ManyToOne
    @JoinColumn(name = "rolfk", nullable = false)
    private Roles rolFK;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties("usuario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PreguntasRespuestas> preguntas;
}