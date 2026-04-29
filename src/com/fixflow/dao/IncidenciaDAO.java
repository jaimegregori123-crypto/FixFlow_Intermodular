package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Incidencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    public List<Incidencia> listarIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
        // 1. Asegúrate de que la columna se llame 'fecha_creacion' en tu DB
        String sql = "SELECT id_incidencia, titulo, descripcion, prioridad, fecha_creacion, id_activo FROM incidencias";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setIdIncidencia(rs.getInt("id_incidencia"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setPrioridad(rs.getString("prioridad"));
                i.setIdActivo(rs.getInt("id_activo"));
                // --- NUEVO: Cargamos la fecha de la DB al objeto ---
                i.setFecha(rs.getDate("fecha_creacion"));

                lista.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertarIncidencia(Incidencia i) {
        // Usamos la fecha que ya viene en el objeto 'i' que seteamos en el Controller
        String sql = "INSERT INTO incidencias (titulo, descripcion, prioridad, fecha_creacion, id_activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, i.getTitulo());
            pstmt.setString(2, i.getDescripcion());
            pstmt.setString(3, i.getPrioridad());
            // --- NUEVO: Enviamos la fecha del objeto ---
            pstmt.setDate(4, i.getFecha());
            pstmt.setInt(5, i.getIdActivo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar incidencia: " + e.getMessage());
            return false;
        }
    }

    // He mantenido este método por si lo usas en otro sitio, pero ahora usa la misma lógica
    public boolean reportarIncidencia(Incidencia inc) {
        return insertarIncidencia(inc);
    }
}
