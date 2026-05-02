package com.fixflow.modelos;

import java.sql.Timestamp;

public class Intervencion {
    private int idIntervencion;
    private int idIncidencia;
    private int idUsuario;
    private Timestamp fecha;
    private String observaciones;
    private int tiempo;

    // NUEVOS campos para mostrar en tabla
    private String nombreUsuario;
    private String tituloIncidencia;

    public Intervencion() {}

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
    public int getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(int idIncidencia) { this.idIncidencia = idIncidencia; }
    public int getIdIntervencion() { return idIntervencion; }
    public void setIdIntervencion(int idIntervencion) { this.idIntervencion = idIntervencion; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public int getTiempo() { return tiempo; }
    public void setTiempo(int tiempo) { this.tiempo = tiempo; }

    // NUEVOS getters y setters
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getTituloIncidencia() { return tituloIncidencia; }
    public void setTituloIncidencia(String tituloIncidencia) { this.tituloIncidencia = tituloIncidencia; }
}
