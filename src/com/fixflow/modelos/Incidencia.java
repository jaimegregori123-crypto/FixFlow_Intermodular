package com.fixflow.modelos;

public class Incidencia {
    private int idIncidencia;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private int idActivo;

    public Incidencia() {}

    public Incidencia(int idIncidencia, String titulo, String descripcion, String prioridad, int idActivo) {
        this.idIncidencia = idIncidencia;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.idActivo = idActivo;
    }

    // GETTERS Y SETTERS (Solo los que existen en tu SQL)
    public int getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(int idIncidencia) { this.idIncidencia = idIncidencia; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int idActivo) { this.idActivo = idActivo; }
}
