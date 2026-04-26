package com.fixflow.main;

import com.fixflow.dao.ActivoDAO;
import com.fixflow.dao.IncidenciaDAO;
import com.fixflow.modelos.Activo;
import com.fixflow.modelos.Incidencia;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // --- 1. SECCIÓN DE ACTIVOS ---
        ActivoDAO activoDAO = new ActivoDAO();
        System.out.println("===========================================");
        System.out.println("   LISTADO DE ACTIVOS (BASE DE DATOS)      ");
        System.out.println("===========================================");

        List<Activo> listaAct = activoDAO.listarActivos();

        if (listaAct == null || listaAct.isEmpty()) {
            System.out.println("⚠️ No hay activos registrados.");
        } else {
            for (Activo a : listaAct) {
                System.out.println("ID: " + a.getIdActivo() + " | Nombre: " + a.getNombre() + " (" + a.getUbicacion() + ")");
            }
        }

        // --- 2. SECCIÓN DE INCIDENCIAS ---
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        System.out.println("\n===========================================");
        System.out.println("   LISTADO DE INCIDENCIAS (AVERÍAS)        ");
        System.out.println("===========================================");

        List<Incidencia> listaInc = incidenciaDAO.listarIncidencias();

        if (listaInc == null || listaInc.isEmpty()) {
            System.out.println("✅ No hay incidencias pendientes.");
        } else {
            for (Incidencia i : listaInc) {
                System.out.println("ID: " + i.getIdIncidencia());
                System.out.println("Título: " + i.getTitulo());
                System.out.println("Prioridad: " + i.getPrioridad());
                System.out.println("ID Activo afectado: " + i.getIdActivo());
                System.out.println("-------------------------------------------");
            }
        }

    }
}
