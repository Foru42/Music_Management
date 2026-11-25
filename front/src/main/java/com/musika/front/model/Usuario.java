package com.musika.front.model;

import java.util.List;

public class Usuario {

    private Integer id;
    private String user;
    private String pass;
    private String salt;
    private Integer intentos = 0;
    private String foto;
    private Role rolFK;
    private List<PreguntaRespuesta> preguntas;

    public List<PreguntaRespuesta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaRespuesta> preguntas) {
        this.preguntas = preguntas;
    }

    public Usuario() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setRolFK(Role rolFK) {
        this.rolFK = rolFK;
    }

    public Integer getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getSalt() {
        return salt;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public String getFoto() {
        return foto;
    }

    public Role getRolFK() {
        return rolFK;
    }

}