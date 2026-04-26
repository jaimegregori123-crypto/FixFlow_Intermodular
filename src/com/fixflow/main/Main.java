package com.fixflow.main;

import com.fixflow.dao.IntervencionDAO;
import com.fixflow.database.Conexion;
import com.fixflow.modelos.Intervencion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IntervencionDAO dao = new IntervencionDAO();

        // 1. Listar lo que hay ahora mismo
        System.out.println("--- LISTADO DE INTERVENCIONES ---");
        List<Intervencion> todas = dao.obtenerIntervenciones();

        for (Intervencion i : todas) {
            System.out.println("ID: " + i.getIdIntervencion() +
                    " | Incidencia: " + i.getIdIncidencia() +
                    " | Nota: " + i.getObservaciones());
        }
    }


}
