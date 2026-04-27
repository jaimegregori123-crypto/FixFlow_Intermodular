package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Incidencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    public boolean reportarIncidencia(Incidencia inc) {
        // SQL ajustado a las columnas reales de tu phpMyAdmin
        String sql = "INSERT INTO incidencias (titulo, descripcion, prioridad, id_activo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inc.getTitulo());
            pstmt.setString(2, inc.getDescripcion());
            pstmt.setString(3, inc.getPrioridad());
            pstmt.setInt(4, inc.getIdActivo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al reportar incidencia: " + e.getMessage());
            return false;
        }
    }

    public List<Incidencia> listarIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
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
                // En tu clase no veo el atributo fecha, si no lo tienes, omite esta línea o añádelo
                i.setIdActivo(rs.getInt("id_activo"));
                lista.add(i);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
