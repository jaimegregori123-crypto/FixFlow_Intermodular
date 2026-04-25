package com.fixflow.modelos;

public class Activo {
    private int idActivo;
    private String nombre;
    private String ubicacion;
    private String estadoOperativo; // Ajustado a tu SQL

    public Activo() {}

    public Activo(int idActivo, String nombre, String ubicacion, String estadoOperativo) {
        this.idActivo = idActivo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.estadoOperativo = estadoOperativo;
    }

    // Getters y Setters
    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int idActivo) { this.idActivo = idActivo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }
}
