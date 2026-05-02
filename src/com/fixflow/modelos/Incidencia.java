package com.fixflow.modelos;

import java.sql.Date;

public class Incidencia {
    private int idIncidencia;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private int idActivo;
    private Date fecha;
    private String estado;

    public Incidencia() {}

    public Incidencia(int idIncidencia, String titulo, String descripcion, String prioridad, int idActivo, Date fecha) {
        this.idIncidencia = idIncidencia;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.idActivo = idActivo;
        this.fecha = fecha;
    }

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
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
