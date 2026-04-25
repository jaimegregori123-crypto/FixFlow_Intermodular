package com.fixflow.main;

import com.fixflow.dao.IncidenciaDAO;
import com.fixflow.modelos.Incidencia;

public class Main {
    public static void main(String[] args) {
        IncidenciaDAO dao = new IncidenciaDAO();

        // El 0 es para el ID (se autogenera), el 1 es el ID del activo (la caldera)
        Incidencia nueva = new Incidencia(0, "Ruido en Caldera", "Hace un soplido fuerte al arrancar", "MEDIA", 1);

        if (dao.reportarIncidencia(nueva)) {
            System.out.println("✅ ¡Incidencia registrada siguiendo tu diagrama!");
        } else {
            System.out.println("❌ Error al insertar.");
        }
    }
}
