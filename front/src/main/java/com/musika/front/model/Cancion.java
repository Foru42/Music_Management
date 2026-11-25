package com.musika.front.model;

public class Cancion {

    private Integer id;
    private String titulo;
    private String enlace;
    private Integer duracion;
    private String descripcion;
    private Boolean reportada;
    private Estilo estiloFK;
    private Usuario usuarioFk;

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    public Cancion() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setReportada(Boolean reportada) {
        this.reportada = reportada;
    }

    public void setEstiloFK(Estilo estiloFK) {
        this.estiloFK = estiloFK;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEnlace() {
        return enlace;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean getReportada() {
        return reportada;
    }

    public Estilo getEstiloFK() {
        return estiloFK;
    }
}
